package com.felstar.calculator

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.felstar.calculator.databinding.ActivityMainBinding
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    var firstNumber: Double = 0.0

    var lastOperator: String? = null

    var numbersPending: Boolean = false

    val myFormatter = DecimalFormat("######.######")

    fun handleOperator(operatorClicked: String) {

        val history = mainBinding.textViewHistory.text.toString()
        val currentResult = mainBinding.textViewResult.text.toString()
        mainBinding.textViewHistory.text = history.plus(currentResult).plus(operatorClicked)

        if (numbersPending) {
            when (lastOperator) {

                "*" -> multiply()
                "/" -> divide()
                "-" -> minus()
                "+" -> plus()
                else -> {
                    firstNumber = getResult()
                }

            }
        }

        lastOperator = operatorClicked
        numbersPending = false
    }

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root

        setContentView(view)

        mainBinding.textViewResult.text = "0"

        mainBinding.btnZero.setOnClickListener {
            onNumberClicked("0")
        }
        mainBinding.btnOne.setOnClickListener {
            onNumberClicked("1")
        }
        mainBinding.btnTwo.setOnClickListener {
            onNumberClicked("2")
        }
        mainBinding.btnThree.setOnClickListener {
            onNumberClicked("3")
        }
        mainBinding.btnFour.setOnClickListener {
            onNumberClicked("4")
        }
        mainBinding.btnFive.setOnClickListener {
            onNumberClicked("5")
        }
        mainBinding.btnSix.setOnClickListener {
            onNumberClicked("6")
        }
        mainBinding.btnSeven.setOnClickListener {
            onNumberClicked("7")
        }
        mainBinding.btnEight.setOnClickListener {
            onNumberClicked("8")
        }
        mainBinding.btnNine.setOnClickListener {
            onNumberClicked("9")
        }

        mainBinding.btnAC.setOnClickListener {
            ac()
        }

        mainBinding.btnDEL.setOnClickListener {
            del()
        }

        mainBinding.btnDivide.setOnClickListener {
            handleOperator("/")
        }

        mainBinding.btnMulti.setOnClickListener {
            handleOperator("*")
        }

        mainBinding.btnMinus.setOnClickListener {
            handleOperator("-")
        }

        mainBinding.btnPlus.setOnClickListener {
            handleOperator("+")
        }

        mainBinding.btnEqual.setOnClickListener {
            handleOperator("=")
        }

        mainBinding.btnDot.setOnClickListener {
            onDotClicked()
        }

        mainBinding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settings_item -> {
                    val intent = Intent(this, ChangeThemeActivity::class.java)
                    startActivity(intent)
                }
            }
            false
        }
    }

    override fun onResume() {
        super.onResume()

        sharedPreferences = this.getSharedPreferences("Dark Theme", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("switch", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }

    override fun onStart() {
        super.onStart()

        sharedPreferences = this.getSharedPreferences("calculations", MODE_PRIVATE)

        mainBinding.textViewResult.text = sharedPreferences.getString("result", "0")
        mainBinding.textViewHistory.text = sharedPreferences.getString("history", "")

        firstNumber = sharedPreferences.getString("first", "0.0")!!.toDouble()
        lastOperator = sharedPreferences.getString("lastOperator", null)
        numbersPending = sharedPreferences.getBoolean("numbersPending", false)
    }

    override fun onPause() {
        super.onPause()

        sharedPreferences = this.getSharedPreferences("calculations", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val resultToSave = mainBinding.textViewResult.text.toString()
        val historyToSave = mainBinding.textViewHistory.text.toString()

        val firstNumberToSave = firstNumber.toString()

        editor.apply {
            putString("result", resultToSave)
            putString("history", historyToSave)
            putString("first", firstNumberToSave)
            putString("lastOperator", lastOperator)
            putBoolean("numbersPending", numbersPending)
            apply()
        }

    }

    fun onDotClicked() {
        val resultString = mainBinding.textViewResult.text.toString()
        if (resultString.contains(".")) return
        onNumberClicked(".")
    }

    fun onNumberClicked(numberClicked: String) {
        var resultString = mainBinding.textViewResult.text.toString()
        resultString = if (resultString.isBlank() || resultString == "0" || !numbersPending) {
            numberClicked
        } else {
            resultString + numberClicked
        }
        mainBinding.textViewResult.text = resultString
        numbersPending = true
    }


    fun getResult(): Double {
        val str = mainBinding.textViewResult.text.toString()
        return if (str.isBlank()) 0.0 else str.toDouble()
    }

    fun plus() {
        firstNumber += getResult()
        mainBinding.textViewResult.text = myFormatter.format(firstNumber)
    }

    fun minus() {
        firstNumber -= getResult()
        mainBinding.textViewResult.text = myFormatter.format(firstNumber)
    }

    fun multiply() {
        firstNumber *= getResult()
        mainBinding.textViewResult.text = myFormatter.format(firstNumber)
    }

    fun divide() {
        if (getResult() == 0.0) {
            Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_LONG).show()
            return
        }
        firstNumber /= getResult()
        mainBinding.textViewResult.text = myFormatter.format(firstNumber)
    }

    fun ac() {
        lastOperator = null
        mainBinding.textViewHistory.text = ""
        mainBinding.textViewResult.text = "0"
        firstNumber = 0.0
    }

    fun del() {
        var numberString = mainBinding.textViewResult.text.toString()

        numberString = numberString.dropLast(1)

        mainBinding.textViewResult.text = numberString
        numbersPending = true
    }
}