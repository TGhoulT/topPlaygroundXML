package com.example.topplaygroundxml.features.auth.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.topplaygroundxml.R
import com.example.topplaygroundxml.databinding.FragmentRegistrationBinding
import com.example.topplaygroundxml.features.auth.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerState.collect { state ->
                    when (state) {
                        is AuthViewModel.RegisterState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.registerButton.isEnabled = false
                            binding.errorText.visibility = View.GONE
                        }
                        is AuthViewModel.RegisterState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.registerButton.isEnabled = true
                            // После успешной регистрации возвращаемся на экран входа
                            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                        }
                        is AuthViewModel.RegisterState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.registerButton.isEnabled = true
                            binding.errorText.text = state.message
                            binding.errorText.visibility = View.VISIBLE
                        }
                        is AuthViewModel.RegisterState.Idle -> {
                            binding.progressBar.visibility = View.GONE
                            binding.registerButton.isEnabled = true
                            binding.errorText.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()
            viewModel.register(email, password, confirmPassword)
        }

        binding.loginTextView.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.clearRegisterState()
    }
}