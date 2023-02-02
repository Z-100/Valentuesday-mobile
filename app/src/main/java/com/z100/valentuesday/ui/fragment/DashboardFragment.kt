package com.z100.valentuesday.ui.fragment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.z100.valentuesday.R
import com.z100.valentuesday.databinding.FragmentDashboardBinding
import com.z100.valentuesday.util.Const.Factory.SP_NAME
import com.z100.valentuesday.api.service.ApiService
import com.z100.valentuesday.service.DataManagerService

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!

    private val apiService = ApiService()

    private var dataManager: DataManagerService? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences(SP_NAME, MODE_PRIVATE)
        dataManager = DataManagerService(sharedPreferences)

        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_question)
        }

        binding.btnQuestionList.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_question_list)
        }

        binding.btnLogOff.setOnClickListener {
            logOff()
        }
    }

    private fun logOff() {
        val actKeyCleared = dataManager!!.clearLoginSharedPreferences()

        if (actKeyCleared)
            findNavController().navigate(R.id.action_dashboard_to_login)

        Snackbar.make(requireView(), "Logging off failed!", 5).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}