package com.example.finaport

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.finaport.R
import android.widget.Button
import android.widget.EditText


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)





        val EMISubmit = findViewById<Button>(R.id.EMICal)
        EMISubmit.setOnClickListener {

            val loanEditText = findViewById<EditText>(R.id.LoanAmount).text.toString().toDoubleOrNull()
            val tenureEditText = findViewById<EditText>(R.id.Tenure).text.toString().toDoubleOrNull()
            val rateEditText = findViewById<EditText>(R.id.Rate).text.toString().toDoubleOrNull()
            if (loanEditText == null || tenureEditText == null || rateEditText == null) {
                return@setOnClickListener
            }


            val intent = Intent(this, Balnce::class.java)
            intent.putExtra("loan", loanEditText)
            intent.putExtra("tenure", tenureEditText)
            intent.putExtra("rate", rateEditText)
            startActivity(intent)

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
}