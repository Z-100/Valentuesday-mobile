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
import com.z100.valentuesday.api.service.ApiRequestService
import com.z100.valentuesday.service.DataManagerService
import com.z100.valentuesday.util.Debug
import com.z100.valentuesday.util.Logger

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!

    private val apiRequestService = ApiRequestService()

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

        gatherProgressData()

        binding.btnResetProgress.setOnClickListener {
            resetProgress();
        }

        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_question)
        }

        binding.btnLogOff.setOnClickListener {
            logOff()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun gatherProgressData() {
        val activationKey = dataManager!!.getActivationKey()
        val jwt = dataManager!!.getAccessToken()

        if (activationKey == null || jwt == null) {
            findNavController().navigate(R.id.action_dashboard_to_login)
            Logger.log("No act-key or jwt found: $activationKey:-:$jwt", this.javaClass)
            return
        }

        if (Debug.isDebug(activationKey)) {
            Debug.counter = dataManager!!.getTotalQuestionProgress() ?: 0

            val progress = 100 / Debug.questionList.size * Debug.counter

            binding.progressText.text = "Total progress: $progress %"
            binding.progressHorizontal.progress = progress.toInt()
            return
        }

        apiRequestService.getTotalQuestionProgress(jwt) { res, err ->
            if (res != null) {
                dataManager!!.updateTotalQuestionProgress(res)

                val progress = 100 / dataManager!!.getAllQuestions()!!.size * res

                binding.progressText.text = "Total progress: $progress %"
                binding.progressHorizontal.progress = progress.toInt()
            } else {
                binding.progressText.text = "Oops! Something went wrong!"
                binding.progressHorizontal.progress = 0
                Logger.log("No total progress found ${err!!.message}", this.javaClass)
            }
        }
    }

    private fun resetProgress() {
        val activationKey = dataManager!!.getActivationKey()
        val jwt = dataManager!!.getAccessToken()

        if (jwt == null || activationKey == null) {
            findNavController().navigate(R.id.action_dashboard_to_login)
            Logger.log("No act-key or jwt found: $activationKey:-:$jwt", this.javaClass)
            return
        }

        if (Debug.isDebug(activationKey)) {
            Logger.log("Resetting progress: $activationKey", this.javaClass)
            dataManager!!.updateTotalQuestionProgress(0)
            Debug.counter = 0
            binding.progressText.text = "Total progress: 0 %"
            binding.progressHorizontal.progress = 0
            return
        }

        apiRequestService.resetTotalQuestionProgress(jwt) { res, err ->
            if (res != null) {
                Logger.log("Resetting progress: Req", this.javaClass)
                dataManager!!.updateTotalQuestionProgress(res)
                binding.progressText.text = "Total progress: $res  %"
                binding.progressHorizontal.progress = res.toInt()
            } else {
                binding.progressText.text = "Oops! Something went wrong!"
                binding.progressHorizontal.progress = 0
                Logger.log("Not able to reset progress ${err!!.message}", this.javaClass)
            }
        }
    }

    private fun logOff() {
        if (dataManager!!.clearAll()) {
            Logger.log("Logging off", this.javaClass)
            findNavController().navigate(R.id.action_dashboard_to_login)
        } else {
            Snackbar.make(requireView(), "Logging off failed!", 5).show()
            Logger.log("Logging off failed", this.javaClass)
        }
    }
}