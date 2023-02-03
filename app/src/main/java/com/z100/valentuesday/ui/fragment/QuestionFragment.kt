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
        }

        apiRequestService.getNextQuestionFor(activationKey!!) { res, err ->
            if (res != null) {
                currentQuestion = res
            } else if (activationKey == "debug") {
                currentQuestion = Question(1L, "Answer 3", 3, "One", "Two", "Three")
            } else {
//                TODO("Handle error accordingly")
            }
        }
    }

    private fun handleAnswer(selectedAnswer: Int) {
        val btnSelected = when (selectedAnswer) {
            1 -> binding.btnAnswerOne
            2 -> binding.btnAnswerTwo
            3 -> binding.btnAnswerThree
            else -> throw RuntimeException(Const.ERROR_IMPOSSIBLE_INPUT)
        }

        if (currentQuestion!!.solution == selectedAnswer) {
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
}