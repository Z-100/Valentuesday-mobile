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
import com.z100.valentuesday.util.Const.Factory.SP_NAME
import com.z100.valentuesday.api.service.ApiRequestService
import com.z100.valentuesday.databinding.FragmentFinishedBinding
import com.z100.valentuesday.service.DataManagerService

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null

    private val binding get() = _binding!!

    private val apiRequestService = ApiRequestService()

    private var dataManager: DataManagerService? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences(SP_NAME, MODE_PRIVATE)
        dataManager = DataManagerService(sharedPreferences)

        binding.btnToDashboard.setOnClickListener {
            findNavController().navigate(R.id.action_finished_to_dashboard)
        }

        binding.btnLogOff.setOnClickListener {
            logOff()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun logOff() {
        val actKeyCleared = dataManager!!.clearAll()

        if (actKeyCleared)
            findNavController().navigate(R.id.action_finished_to_login)
        else
            Snackbar.make(requireView(), "Logging off failed!", 5).show()
    }
}