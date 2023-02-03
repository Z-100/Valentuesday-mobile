package com.z100.valentuesday.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.z100.valentuesday.MainActivity
import com.z100.valentuesday.R
import com.z100.valentuesday.api.service.ApiRequestService
import com.z100.valentuesday.api.components.Question
import com.z100.valentuesday.databinding.FragmentQuestionBinding
import com.z100.valentuesday.service.DataManagerService
import com.z100.valentuesday.util.Const
import com.z100.valentuesday.util.Debug
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

        val activationKey = dataManager!!.getActivationKey()

        if (activationKey == null) {
            findNavController().navigate(R.id.action_dashboard_to_login)
            TODO("This is bs")
        }

        apiRequestService.getNextQuestionFor(activationKey) { res, err ->
            if (res != null) {
                currentQuestion = res
                fillInFields(currentQuestion!!)
            } else if (activationKey == "debug") {
                if (Debug.counter < Debug.questionList.size) {
                    currentQuestion = Debug.questionList[Debug.counter.toInt()]
                    fillInFields(currentQuestion!!)
                } else {
                    findNavController().navigate(R.id.action_question_to_dashboard)
                }
            } else {
//              TODO("Handle error accordingly")
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

        if (activationKey == null) {
            findNavController().navigate(R.id.action_dashboard_to_login)
            TODO("This is bs")
        }

        apiRequestService.updateTotalQuestionProgress(activationKey) { res, err ->
            if (res != null) {
                dataManager!!.updateTotalQuestionProgress(res)
            } else if (activationKey == "debug") {
                Debug.counter++
                dataManager!!.updateTotalQuestionProgress(Debug.counter)
            } else {
                TODO("Handle this bs")
            }
        }
    }
}