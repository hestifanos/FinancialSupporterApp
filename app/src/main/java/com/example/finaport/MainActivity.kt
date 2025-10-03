package com.example.finaport

import android.annotation.SuppressLint
import android.content.Intent
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


class MainActivity : AppCompatActivity() {
    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        // First Button: Continue to Balance Calculation
        // This button gathers the loan details and passes them to the 'Balance' activity.
        val emiSubmit = findViewById<Button>(R.id.EMICal)
        emiSubmit.setOnClickListener {

            // Get user input from EditText fields and safely convert to a number (Double).
            // .toDoubleOrNull() returns null if the input is not a valid number, preventing a crash.
            val loanEditText = findViewById<EditText>(R.id.LoanAmount).text.toString().toDoubleOrNull()
            val tenureEditText = findViewById<EditText>(R.id.Tenure).text.toString().toDoubleOrNull()
            val rateEditText = findViewById<EditText>(R.id.Rate).text.toString().toDoubleOrNull()

            //  Input Validation
            // Check if any of the fields are empty or contain invalid input.
            if (loanEditText == null || tenureEditText == null || rateEditText == null) {
                // Show a pop-up message (Toast) to the user asking for valid input.
                Toast.makeText(this, "Please enter valid numbers in all fields", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            //  Navigate to Balance Activity
            // Create an Intent to start the Balnce activity.
            val intent = Intent(this, Balnce::class.java)
            // Add the user-provided data to the intent so the next activity can use it.
            intent.putExtra("loan", loanEditText)
            intent.putExtra("tenure", tenureEditText)
            intent.putExtra("rate", rateEditText)
            // Start the new activity.
            startActivity(intent)
        }

        // Second Button: Calculate EMI on this screen
        val calculateButton = findViewById<Button>(R.id.EMIIterResul)
        calculateButton.setOnClickListener {

            // Get the user input from the EditText fields.
            val loanEditText = findViewById<EditText>(R.id.LoanAmount).text.toString().toDoubleOrNull()
            val tenureEditText = findViewById<EditText>(R.id.Tenure).text.toString().toDoubleOrNull()
            val rateEditText = findViewById<EditText>(R.id.Rate).text.toString().toDoubleOrNull()

            // EMI Calculation
            val monthlyRate = rateEditText?.div(1200) // Monthly interest rate.
            val numberOfMonths = tenureEditText

            val emiNumerator = monthlyRate?.times((1 + monthlyRate).pow(numberOfMonths!!))
            val emiDenominator = (1 + monthlyRate!!).pow(numberOfMonths!!) - 1

            // Prevent division by zero.
            if (emiDenominator == 0.0) {
                Toast.makeText(this, "Cannot calculate EMI with these tenure/rate values (division by zero)", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            // The intended calculation is: emi = loanEditText * (emiNumerator / emiDenominator)
            val emi = loanEditText?.times(emiNumerator!!)?.div(emiDenominator)
            emiDenominator

            //  Display EMI Result
            val resultTextView = findViewById<TextView>(R.id.OnlyEMIInterResult)
            // Display the calculated EMI in a TextView, formatted to two decimal places.
            resultTextView.text = String.format("EMI: %.2f", emi)

        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}