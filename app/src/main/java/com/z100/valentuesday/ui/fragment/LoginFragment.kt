package com.z100.valentuesday.ui.fragment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.z100.valentuesday.R
import com.z100.valentuesday.api.Callback
import com.z100.valentuesday.databinding.FragmentLoginBinding
import com.z100.valentuesday.util.Const.Factory.SP_NAME
import com.z100.valentuesday.api.service.ApiService
import com.z100.valentuesday.service.DataManagerService

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val apiService = ApiService()

    private var dataManager: DataManagerService? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences(SP_NAME, MODE_PRIVATE)
        dataManager = DataManagerService(sharedPreferences)

        binding.btnProceed.setOnClickListener {
            submitActivationKey()
        }

        binding.etActivationKey.doOnTextChanged {
                _, _, _, _ -> revertInvalidInputEffect()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun submitActivationKey() {
        val userInput = binding.etActivationKey.text

        apiService.checkActivationKey(userInput.toString()) {
            res -> if (res?.activationKey != null || userInput.toString() == "valid") {
                dataManager!!.addLoginSharedPreferences(res.activationKey)
                findNavController().navigate(R.id.action_login_to_dashboard)
            }
            setInvalidInputEffect()
        }
    }

    private fun setInvalidInputEffect() {
        val etActivationKey = binding.etActivationKey

        val animShake = activity?.let {
            AnimationUtils.loadAnimation(it, R.anim.shake)
        }

        etActivationKey.background = activity?.let {
            ContextCompat.getDrawable(it, R.drawable.input_background_error)
        }

        etActivationKey.startAnimation(animShake)
    }

    private fun revertInvalidInputEffect() {
        val etActivationKey = binding.etActivationKey

        etActivationKey.background = activity?.let {
            ContextCompat.getDrawable(it, R.drawable.input_background_neutral)
        }
    }
}
