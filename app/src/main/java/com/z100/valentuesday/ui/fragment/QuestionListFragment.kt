package com.z100.valentuesday.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.z100.valentuesday.api.service.ApiRequestService
import com.z100.valentuesday.api.components.Question
import com.z100.valentuesday.databinding.FragmentQuestionListBinding
import com.z100.valentuesday.service.DataManagerService
import com.z100.valentuesday.util.Const

class QuestionListFragment : Fragment() {

    private var _binding: FragmentQuestionListBinding? = null

    private val binding get() = _binding!!

    private val apiRequestService = ApiRequestService()

    private var dataManager: DataManagerService? = null

    private var currentQuestion: Question? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentQuestionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences(Const.SP_NAME, Context.MODE_PRIVATE)
        dataManager = DataManagerService(sharedPreferences)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}