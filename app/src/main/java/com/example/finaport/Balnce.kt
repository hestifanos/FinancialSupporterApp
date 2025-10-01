package com.example.finaport

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.pow

class Balnce : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_balnce)

        val submitButton = findViewById<Button>(R.id.submit)
        val resultTextView = findViewById<TextView>(R.id.result)
        val loanFromMain = intent.getDoubleExtra("loan", 0.0)
        val tenureFromMain = intent.getDoubleExtra("tenure", 0.0)
        val rateFromMain = intent.getDoubleExtra("rate", 0.0)

        val monthlyIncomeEditText = findViewById<EditText>(R.id.MonthlyIncome)
        val expenseEditText = findViewById<EditText>(R.id.Expense)

        submitButton.setOnClickListener {

            val monthlyIncomeString = monthlyIncomeEditText.text.toString()
            val expenseString = expenseEditText.text.toString()

            val loan = loanFromMain
            val tenure = tenureFromMain
            val rate = rateFromMain


            val monthlyIncome = monthlyIncomeString.toDoubleOrNull()
            val expense = expenseString.toDoubleOrNull()

            if (monthlyIncome == null || expense == null) {
                Toast.makeText(this, "Please enter valid numbers in all fields", Toast.LENGTH_LONG).show()// this is a toast message which means a small pop-up message
                return@setOnClickListener
            }

            if (rate <= 0) {
                Toast.makeText(this, "Interest rate must be positive", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (tenure <= 0) {
                Toast.makeText(this, "Tenure must be positive", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // EMI Calculation
            val monthlyRate = rate / 1200 // Monthly interest rate (e.g., if rate is 10%, then 10/1200)
            val numberOfMonths = tenure

            val emiNumerator = monthlyRate * (1 + monthlyRate).pow(numberOfMonths)
            val emiDenominator = (1 + monthlyRate).pow(numberOfMonths) - 1

            if (emiDenominator == 0.0) {
                Toast.makeText(this, "Cannot calculate EMI with these tenure/rate values (division by zero)", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val emi = loan * emiNumerator / emiDenominator

            val finalBalance = monthlyIncome - emi - expense

            if (finalBalance < 0)
            {
                resultTextView.text = String.format(
                    "Hmm, this loan might make things a bit tight. Your monthly balance would be: %.2f",
                    finalBalance)
            }
            else
            resultTextView.text = String.format("🎉 Awesome! Your are on a saving track: %.2f", finalBalance) // Format to 2 decimal places // Format to 2 decimal places
        }

        // Ensure R.id.main exists in your R.layout.activity_balnce
        // and is the root view or a suitable view for applying window insets.
        val mainView = findViewById<android.view.View>(R.id.main) // Assuming R.id.main is the ID of your root layout
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}