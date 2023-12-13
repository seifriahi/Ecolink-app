package com.ecolink.ui
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ecolink.R
import com.ecolink.viewmodel.SignupViewModel

class SignupActivity : AppCompatActivity() {

    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        // Find your UI elements
        val fullnameEditText: EditText = findViewById(R.id.editText5)
        val emailEditText: EditText = findViewById(R.id.editText1)
        val passwordEditText: EditText = findViewById(R.id.editText2)
        val confirmPasswordEditText: EditText = findViewById(R.id.editText3)

        /*val dateOfBirthEditText: DatePicker = findViewById(R.id.datePicker)
        val day = dateOfBirthEditText.dayOfMonth
        val month = dateOfBirthEditText.month + 1  // Months are 0-indexed, so add 1
        val year = dateOfBirthEditText.year*/
        val roleEditText: Spinner = findViewById(R.id.spinnerUserType)
        //val selectedRole: String = roleEditText.selectedItem.toString() // Assuming the spinner is used for role selection
        val signupButton: Button = findViewById(R.id.loginButton) // Assuming the loginButton is used for signup

        // Observe the signup response
        signupViewModel.signupResponse.observe(this, { response ->
            // Handle the response, update UI accordingly
            setContentView(R.layout.loading_screen)
            if (response.isSuccessful) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                setContentView(R.layout.register_page)
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        // Set up click listener for the signup button
        signupButton.setOnClickListener {
            setContentView(R.layout.loading_screen)
            val fullname = fullnameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            //val dateOfBirth = "$year-$month-$day"
            val role = roleEditText.selectedItem.toString() // Assuming you want the selected item from the spinner
            if (!isValidEmail(email)) {
                // Notify the user that the email format is invalid
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate alphabetical characters in the full name
            if (!isAlphabetical(fullname)) {
                // Notify the user that the full name should be strictly alphabetical
                Toast.makeText(this, "Full name should be strictly alphabetical", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate minimum password length
            if (password.length < 8) {
                // Notify the user that the password should be at least 8 characters long
                Toast.makeText(this, "Password should be at least 8 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Trigger the signup function in the ViewModel
            if (password == confirmPassword) {
                // Trigger the signup function in the ViewModel
                signupViewModel.signupUser(fullname, email, password, role)
            } else {
                // Notify the user that passwords do not match
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    // Function to check if the string is strictly alphabetical
    private fun isAlphabetical(str: String): Boolean {
        return str.matches("[a-zA-Z]+".toRegex())
    }
}

