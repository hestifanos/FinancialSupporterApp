package com.example.finaport

import android.annotation.SuppressLint
import android.graphics.Color
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
    // Suppressing warnings for MissingInflatedId (handled by findViewById) and DefaultLocale for String.format.
    @SuppressLint("MissingInflatedId", "DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_balnce)

        // View Initialization
        // Get references to the UI elements (Button, TextView, EditText) from the XML layout.
        val submitButton = findViewById<Button>(R.id.submit)
        val resultTextView = findViewById<TextView>(R.id.result)

        //  Data Retrieval
        // Get the loan details (loan amount, tenure, rate) passed from MainActivity via an Intent.
        val loanFromMain = intent.getDoubleExtra("loan", 0.0)
        val tenureFromMain = intent.getDoubleExtra("tenure", 0.0)
        val rateFromMain = intent.getDoubleExtra("rate", 0.0)

        val monthlyIncomeEditText = findViewById<EditText>(R.id.MonthlyIncome)
        val expenseEditText = findViewById<EditText>(R.id.Expense)

        //  Submit Button
        // Set a listener to execute code when the submit button is clicked.
        submitButton.setOnClickListener {

            // Get user input from EditTexts as a string.
            val monthlyIncomeString = monthlyIncomeEditText.text.toString()
            val expenseString = expenseEditText.text.toString()

            // Assign the values from the intent to local variables for clarity.
            val loan = loanFromMain
            val tenure = tenureFromMain
            val rate = rateFromMain

            // Input Parsing and Validation
            // Safely convert the input strings to numbers (Double).
            // .toDoubleOrNull() returns null if the string is not a valid number, preventing crashes.
            val monthlyIncome = monthlyIncomeString.toDoubleOrNull()
            val expense = expenseString.toDoubleOrNull()

            // Check if any input field is empty or contains non-numeric text.
            if (monthlyIncome == null || expense == null) {
                // A Toast is a small pop-up message to inform the user.
                Toast.makeText(this, "Please enter valid numbers in all fields", Toast.LENGTH_LONG).show()
                // Stop further execution of the listener if input is invalid.
                return@setOnClickListener
            }

            // Validate that the rate and tenure from the previous screen are positive, as they are used in calculations.
            if (rate <= 0) {
                Toast.makeText(this, "Interest rate must be positive", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (tenure <= 0) {
                Toast.makeText(this, "Tenure must be positive", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            //  Core Calculation
            // 1. EMI (Equated Monthly Installment) Calculation.
            val monthlyRate = rate / 1200 // Convert annual percentage rate to a monthly decimal factor.
            val numberOfMonths = tenure

            // Standard formula for calculating EMI.
            val emiNumerator = monthlyRate * (1 + monthlyRate).pow(numberOfMonths)
            val emiDenominator = (1 + monthlyRate).pow(numberOfMonths) - 1

            // Critical check to prevent division by zero, which would cause the app to crash.
            if (emiDenominator == 0.0) {
                Toast.makeText(this, "Cannot calculate EMI with these tenure/rate values (division by zero)", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val emi = loan * emiNumerator / emiDenominator

            // 2. Final Balance Calculation.
            val finalBalance = monthlyIncome - emi - expense

            //  Displaying the Result
            // Check if the final balance is negative or positive and update the UI accordingly.
            if (finalBalance < 0)
            {

                resultTextView.text = String.format(
                    "Infeasible loan amount. \nMonthly Balance: %.2f",
                    finalBalance)
                // Set the text color to red to indicate a negative or unrecommended result.
                resultTextView.setTextColor(Color.RED)
            }
            else
            {
                resultTextView.text = String.format("Feasible loan amount. \nMonthly Balance: %.2f", finalBalance)
                // Set the text color to a dark green to indicate a positive or recommended result.
                resultTextView.setTextColor(Color.parseColor("#2E7D32"))
            }
        }

        // Navigation
        // Get a reference to the 'back' button.
        val backButton = findViewById<Button>(R.id.back)

        // Set a listener to navigate back to MainActivity when the button is clicked.
        backButton.setOnClickListener {
            // An Intent describes what an app wants to do. Here, it's used to start a new activity.
            val intent = android.content.Intent(this, MainActivity::class.java)
            startActivity(intent)
        }



        val mainView = findViewById<android.view.View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
