package com.example.learningjetpackcompose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel: ViewModel() {
    var state by mutableStateOf(CalculatorState())
    private set

    fun onAction(actions: CalculatorActions){
        when(actions){
            is CalculatorActions.Number -> enterNumber(actions.number)
            is CalculatorActions.Calculate -> performCalculation()
            is CalculatorActions.Clear -> state = CalculatorState()
            is CalculatorActions.Decimal -> enterDecimal()
            is CalculatorActions.Delete -> performDeletion()
            is CalculatorActions.Operation -> enterOperation(actions.operation)
        }
    }

    private fun performDeletion() {
        when{
            state.num2.isNotBlank() -> state = state.copy(
                num2 =  state.num2.dropLast(1)
            )
            state.operation != null -> state = state.copy(
                operation = null
            )
            state.num1.isNotBlank() -> state = state.copy(
                num1 = state.num1.dropLast(1)
            )
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if(state.num1.isNotBlank()){
            state = state.copy(operation = operation)
        }
    }

    private fun enterDecimal() {
        if(state.operation == null && !state.num1.contains(".") && state.num1.isNotBlank()
            ){
            state = state.copy(
                num1 = state.num1 + "."
            )
            return
        }
        if(!state.num2.contains(".") && state.num2.isNotBlank()
        ){
            state = state.copy(
                num2 = state.num2 + "."
            )
            return
        }
    }

    private fun performCalculation() {
        val num1 = state.num1.toDoubleOrNull()
        val num2 = state.num2.toDoubleOrNull()
        if(num1 != null && num2 != null){
            val result = when(state.operation) {
                is CalculatorOperation.Add -> num1 + num2
                is CalculatorOperation.Subtract -> num1 - num2
                is CalculatorOperation.Multiply -> num1 * num2
                is CalculatorOperation.Divide -> num1 / num2
                null -> return
            }
            state = state.copy(
                num1 = result.toString().take(15),
                operation = null,
                num2 = ""
            )
        }
    }

    private fun enterNumber(number: Int) {
        if(state.operation == null){
            if(state.num1.length >= MAX_NUM_LENGTH){
                return
            }
            state = state.copy(
                num1 = state.num1 + number
            )
            return
        }
        if(state.num2.length >= MAX_NUM_LENGTH){
            return
        }
        state = state.copy(
            num2 = state.num2 + number
        )
        return
    }
    companion object{
        private const val MAX_NUM_LENGTH = 8
    }
}