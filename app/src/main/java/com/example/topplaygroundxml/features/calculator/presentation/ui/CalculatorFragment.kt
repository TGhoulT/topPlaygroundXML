package com.example.topplaygroundxml.features.calculator.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.topplaygroundxml.databinding.FragmentCalculatorBinding
import com.example.topplaygroundxml.features.calculator.presentation.viewmodel.CalculatorViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CalculatorViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
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
                viewModel.expression.collect { expression ->
                    binding.expressionText.text = expression
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.result.collect { result ->
                    binding.resultText.text = result
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collect { error ->
                    error?.let {
                        binding.resultText.text = it
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        // Цифры
        binding.button0.setOnClickListener { viewModel.onNumberClick("0") }
        binding.button00.setOnClickListener { viewModel.onNumberClick("00") }
        binding.button1.setOnClickListener { viewModel.onNumberClick("1") }
        binding.button2.setOnClickListener { viewModel.onNumberClick("2") }
        binding.button3.setOnClickListener { viewModel.onNumberClick("3") }
        binding.button4.setOnClickListener { viewModel.onNumberClick("4") }
        binding.button5.setOnClickListener { viewModel.onNumberClick("5") }
        binding.button6.setOnClickListener { viewModel.onNumberClick("6") }
        binding.button7.setOnClickListener { viewModel.onNumberClick("7") }
        binding.button8.setOnClickListener { viewModel.onNumberClick("8") }
        binding.button9.setOnClickListener { viewModel.onNumberClick("9") }

        // Операции
        binding.buttonAdd.setOnClickListener { viewModel.onOperationClick("+") }
        binding.buttonSubtract.setOnClickListener { viewModel.onOperationClick("-") }
        binding.buttonMultiply.setOnClickListener { viewModel.onOperationClick("×") }
        binding.buttonDivide.setOnClickListener { viewModel.onOperationClick("÷") }

        // Процент
        binding.buttonPercent.setOnClickListener { viewModel.onPercentageClick() }

        // Функциональные кнопки
        binding.buttonDecimal.setOnClickListener { viewModel.onDecimalClick() }
        binding.buttonClear.setOnClickListener { viewModel.onClearClick() }
        binding.buttonDelete.setOnClickListener { viewModel.onDeleteClick() }
        binding.buttonEquals.setOnClickListener { viewModel.onEqualsClick() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}