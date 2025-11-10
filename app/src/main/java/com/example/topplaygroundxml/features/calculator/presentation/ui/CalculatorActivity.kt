package com.example.topplaygroundxml.features.calculator.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.topplaygroundxml.databinding.ActivityCalculatorBinding
import com.example.topplaygroundxml.features.calculator.presentation.viewmodel.CalculatorViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalculatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorBinding
    private val viewModel: CalculatorViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupObservers()
        setupListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.expression.collectLatest { expression ->
                    binding.expressionText.text = expression
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.result.collectLatest { result ->
                    binding.resultText.text = result
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collectLatest { error ->
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

        // Процент - отдельный обработчик
        binding.buttonPercent.setOnClickListener { viewModel.onPercentageClick() }

        // Функциональные кнопки
        binding.buttonDecimal.setOnClickListener { viewModel.onDecimalClick() }
        binding.buttonClear.setOnClickListener { viewModel.onClearClick() }
        binding.buttonDelete.setOnClickListener { viewModel.onDeleteClick() }
        binding.buttonEquals.setOnClickListener { viewModel.onEqualsClick() }
    }

}