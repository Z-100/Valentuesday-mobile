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
import com.z100.valentuesday.databinding.FragmentLoginBinding
import com.z100.valentuesday.util.Const.Factory.SP_NAME
import com.z100.valentuesday.api.service.ApiRequestService
import com.z100.valentuesday.service.DataManagerService

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val apiRequestService = ApiRequestService()

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

        skipLoginIfActivationKeyPresent()

        binding.btnProceed.setOnClickListener {
            submitActivationKey()
        }

        binding.etActivationKey.doOnTextChanged { _, _, _, _ ->
            revertInvalidInputEffect()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun skipLoginIfActivationKeyPresent() {
        if (dataManager!!.getActivationKey() != null)
            findNavController().navigate(R.id.action_login_to_dashboard)
    }

    private fun submitActivationKey() {
        val userInput = binding.etActivationKey.text

        apiRequestService.checkActivationKey(userInput.toString()) { res, err ->
            if (res?.activationKey != null) {
                dataManager!!.addActivationKey(res.activationKey)
                findNavController().navigate(R.id.action_login_to_dashboard)
            } else if (userInput.toString() == "debug") {
                dataManager!!.addActivationKey(userInput.toString())
                findNavController().navigate(R.id.action_login_to_dashboard)
//                TODO("remove this code, or let it be for debug mode")
            } else {
                setInvalidInputEffect(err?.message)
            }
        }
    }

    private fun setInvalidInputEffect(msg: String? = "Activation key invalid") {
        val etActivationKey = binding.etActivationKey

        val animShake = activity?.let {
            AnimationUtils.loadAnimation(it, R.anim.shake)
        }

        etActivationKey.background = activity?.let {
            ContextCompat.getDrawable(it, R.drawable.input_background_error)
        }

        etActivationKey.hint = activity?.let {
            getString(R.string.login_invalid_activation_key)
        }

        etActivationKey.startAnimation(animShake)
    }

    private fun revertInvalidInputEffect() {
        val etActivationKey = binding.etActivationKey

        etActivationKey.background = activity?.let {
            ContextCompat.getDrawable(it, R.drawable.input_background_neutral)
        }

        etActivationKey.hint = activity?.let {
            getString(R.string.login_enter_activation_key)
        }
    }
}
