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
import com.google.android.material.snackbar.Snackbar
import com.z100.valentuesday.R
import com.z100.valentuesday.databinding.FragmentLoginBinding
import com.z100.valentuesday.util.Const.Factory.SP_NAME
import com.z100.valentuesday.api.service.ApiRequestService
import com.z100.valentuesday.service.DataManagerService
import com.z100.valentuesday.util.Debug
import com.z100.valentuesday.util.Logger

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

        val activationKey = dataManager!!.getActivationKey()
        val hasToken = dataManager!!.getAccessToken() != null

        Logger.log("Activation key: $activationKey | Has token: $hasToken", this.javaClass)

        if (!activationKey.isNullOrBlank() && (hasToken || Debug.isDebug(activationKey)))
            getAllQuestions()
    }

    private fun submitActivationKey() {
        val userInputActKey = binding.etActivationKey.text

        if (Debug.isDebug(userInputActKey.toString())) {
            dataManager!!.addActivationKey(userInputActKey.toString())
            dataManager!!.addAccessToken(userInputActKey.toString())
            Logger.log("Debug activation key added", this.javaClass)
            getAllQuestions()
            return
        }

        apiRequestService.checkActivationKey(userInputActKey.toString()) { res, err ->
            if (res?.jwt != null) {
                dataManager!!.addActivationKey(userInputActKey.toString())
                dataManager!!.addAccessToken(res.jwt)
                getAllQuestions()
            } else {
                setInvalidInputEffect(err?.message)
                Logger.log("Error retrieving login: ${err?.message}", this.javaClass)
            }
        }
    }

    private fun getAllQuestions() {
        val activationKey = dataManager!!.getActivationKey()
        val jwt = dataManager!!.getAccessToken()

        if (activationKey == null || jwt == null) {
            Logger.log("Get all questions failed: $activationKey:$jwt", this.javaClass)
            return
        }

        if (Debug.isDebug(activationKey)) {
            dataManager!!.addAllQuestions(Debug.questionList)
            Logger.log("Debug question list gathered", this.javaClass)
            findNavController().navigate(R.id.action_login_to_dashboard)
            return
        }

        apiRequestService.getAllQuestionsFor(jwt) { res, err ->
            if (res != null) {
                dataManager!!.addAllQuestions(res)
                findNavController().navigate(R.id.action_login_to_dashboard)
            } else {
                view?.let { Snackbar.make(it, "No questions found!", 2).show() }
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
