package com.example.icalculatorwithstaticlayout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Number(): ViewModel(){
    val number = MutableLiveData<String>("")

    private var operand1: Double = 0.0
    private var pendingOperation: String = ""

    fun onNumberPressed(digit: String){
        if (number.value == "Error") number.value = ""

        if (digit == "." && ultimoNumeroContienePunto()) return

        number.value = (number.value ?: "") + digit
    }

    fun onOperationPressed(op: String) {
        val currentText = number.value ?: ""

        if (currentText.isNotEmpty() && !currentText.last().toString().contains(Regex("[+\\-*/]"))) {
            operand1 = currentText.toDoubleOrNull() ?: 0.0
            pendingOperation = op
            number.value = currentText + op
        }
    }

    fun onChangeSign() {
        val current = number.value ?: ""
        if (current.isNotEmpty() && current != "Error") {
            // Si ya tiene un signo negativo, se quita; de lo contrario, se añade[cite: 1]
            number.value = if (current.startsWith("-")) {
                current.substring(1)
            } else {
                "-$current"
            }
        }
    }

    fun onPercentage() {
        val current = number.value?.toDoubleOrNull() ?: return
        number.value = (current / 100.0).toString()
    }

    fun onCalculate() {
        val fullExpression = number.value ?: ""
        if (fullExpression.isEmpty() || pendingOperation.isEmpty()) return


        val parts = fullExpression.split(pendingOperation)
        if (parts.size < 2 || parts[1].isEmpty()) return

        val operand2 = parts[1].toDoubleOrNull() ?: 0.0

        val result = when (pendingOperation) {
            "+" -> operand1 + operand2
            "-" -> operand1 - operand2
            "*" -> operand1 * operand2
            "/" -> if (operand2 != 0.0) operand1 / operand2 else null
            else -> operand2
        }

        if (result == null) {
            number.value = "Error"
        } else {
            // Se limpia el formato: si es un número entero, se elimina el ".0"[cite: 1]
            val resultString = if (result % 1 == 0.0) result.toLong().toString() else result.toString()
            number.value = resultString
        }
        pendingOperation = ""
    }

    private fun ultimoNumeroContienePunto(): Boolean {
        val current = number.value ?: ""
        val lastPart = current.split(Regex("[+\\-*/]")).last()
        return lastPart.contains(".")
    }

    fun onClear() {
        number.value = ""
        operand1 = 0.0
        pendingOperation = ""
    }
}