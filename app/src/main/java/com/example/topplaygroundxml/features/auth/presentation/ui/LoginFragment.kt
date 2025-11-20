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
import com.example.topplaygroundxml.databinding.FragmentLoginBinding
import com.example.topplaygroundxml.features.auth.presentation.viewmodel.AuthViewModel
import com.example.topplaygroundxml.features.auth.presentation.viewmodel.state.LoginState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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
                viewModel.loginState.collect { state ->
                    when (state) {
                        is LoginState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.loginButton.isEnabled = false
                        }
                        is LoginState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            findNavController().navigate(R.id.action_loginFragment_to_weatherFragment)
                        }
                        is LoginState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.loginButton.isEnabled = true
                            binding.errorText.text = state.message
                            binding.errorText.visibility = View.VISIBLE
                        }
                        else -> {
                            binding.progressBar.visibility = View.GONE
                            binding.loginButton.isEnabled = true
                        }
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.login(email, password)
        }

        binding.registerTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.clearLoginState()
    }
}