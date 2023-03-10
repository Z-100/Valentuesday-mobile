package com.z100.valentuesday.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.z100.valentuesday.R
import com.z100.valentuesday.api.service.ApiRequestService
import com.z100.valentuesday.api.components.Question
import com.z100.valentuesday.databinding.FragmentQuestionBinding
import com.z100.valentuesday.service.DataManagerService
import com.z100.valentuesday.util.Const
import com.z100.valentuesday.util.Debug
import com.z100.valentuesday.util.Logger
import java.lang.RuntimeException

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null

    private val binding get() = _binding!!

    private val apiRequestService = ApiRequestService()

    private var dataManager: DataManagerService? = null

    private var currentQuestion: Question? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences(Const.SP_NAME, Context.MODE_PRIVATE)
        dataManager = DataManagerService(sharedPreferences)

        gatherQuestionData()

        binding.btnGoBack.setOnClickListener {
            findNavController().navigate(R.id.action_question_to_dashboard)
        }

        binding.btnAnswerOne.setOnClickListener {
            handleAnswer(1)
        }

        binding.btnAnswerTwo.setOnClickListener {
            handleAnswer(2)
        }

        binding.btnAnswerThree.setOnClickListener {
            handleAnswer(3)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun gatherQuestionData() {
        Logger.log("Gathering question data", this.javaClass)

        if (dataManager!!.allQuestionsExists()) {
            val progress = dataManager!!.getTotalQuestionProgress() ?: 0

            val questions = dataManager!!.getAllQuestions()

            if (questions != null && progress < questions.size) {
                currentQuestion = dataManager!!.getSpecificQuestion(progress!!.toInt())
                fillInFields(currentQuestion!!)
                Logger.log("$progress -> $currentQuestion", this.javaClass)
            } else {
                Logger.log("All questions finished", this.javaClass)
                findNavController().navigate(R.id.action_question_to_finished)
            }
            return
        }

        val activationKey = dataManager!!.getActivationKey()
        val jwt = dataManager!!.getAccessToken()

        if (jwt == null || activationKey == null) {
            findNavController().navigate(R.id.action_dashboard_to_login)
            Logger.log("No act-key or jwt found: $activationKey:-:$jwt", this.javaClass)
            return
        }

        if (Debug.isDebug(activationKey)) {
            if (Debug.counter < Debug.questionList.size) {
                currentQuestion = Debug.questionList[Debug.counter.toInt()]
                fillInFields(currentQuestion!!)
                Logger.log("Next question: $currentQuestion", this.javaClass)
            } else {
                Logger.log("All questions finished", this.javaClass)
                findNavController().navigate(R.id.action_question_to_finished)
            }
            return
        }

        apiRequestService.getNextQuestionFor(jwt) { res, err ->
            if (res != null) {
                currentQuestion = res
                fillInFields(currentQuestion!!)
                Logger.log("Next question: $currentQuestion", this.javaClass)
            } else {
                if ( err!!.networkResponse.statusCode == 409) {
                    Logger.log("All questions finished", this.javaClass)
                    findNavController().navigate(R.id.action_question_to_finished)
                }
            }
        }
    }

    private fun fillInFields(question: Question) {
        binding.tvQuestion.text = question.question
        binding.btnAnswerOne.text = question.answerOne
        binding.btnAnswerTwo.text = question.answerTwo
        binding.btnAnswerThree.text = question.answerThree
    }

    private fun handleAnswer(selectedAnswer: Int) {
        val btnSelected = when (selectedAnswer) {
            1 -> binding.btnAnswerOne
            2 -> binding.btnAnswerTwo
            3 -> binding.btnAnswerThree
            else -> throw RuntimeException(Const.ERROR_IMPOSSIBLE_INPUT)
        }

        if (currentQuestion!!.solution == selectedAnswer) {
            updateTotalQuestionProgress()
            btnSelected.background = activity?.let {
                ContextCompat.getDrawable(it, R.drawable.input_background_success)
            }
            findNavController().navigate(R.id.action_question_to_question)
        } else {
            val animShake = activity?.let {
                AnimationUtils.loadAnimation(it, R.anim.shake)
            }

            btnSelected.background = activity?.let {
                ContextCompat.getDrawable(it, R.drawable.input_background_error)
            }

            btnSelected.startAnimation(animShake)
        }
    }

    private fun updateTotalQuestionProgress() {
        val activationKey = dataManager!!.getActivationKey()
        val jwt = dataManager!!.getAccessToken()

        if (jwt == null || activationKey == null) {
            Logger.log("No act-key or jwt found: $activationKey:-:$jwt", this.javaClass)
            findNavController().navigate(R.id.action_dashboard_to_login)
            return
        }

        var newProgress = dataManager!!.getTotalQuestionProgress() ?: 0
        dataManager!!.updateTotalQuestionProgress(++newProgress)

        if (Debug.isDebug(activationKey)) {
            Debug.counter++
            dataManager!!.updateTotalQuestionProgress(Debug.counter)
            return
        }

        apiRequestService.updateTotalQuestionProgress(jwt) { res, err ->
            if (res != null) {
                dataManager!!.updateTotalQuestionProgress(res)
                Logger.log("Tot question progress updated: $res", this.javaClass)
            } else {
                if (err!!.networkResponse.statusCode == 409)
                    findNavController().navigate(R.id.action_question_to_finished)
            }
        }
    }
}