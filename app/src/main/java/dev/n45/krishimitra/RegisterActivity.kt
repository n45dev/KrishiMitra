package dev.n45.krishimitra

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import dev.n45.krishimitra.api.Client
import dev.n45.krishimitra.api.data.User
import dev.n45.krishimitra.api.data.UserRegisterRequest
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginButton = findViewById<Button>(R.id.login_button)
        val registerButton = findViewById<Button>(R.id.register_button)

        val emailText = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.email)
        val passwordText = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.password)
        val confirmPasswordText = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.repeatPassword)
        val nameText = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.name)

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        registerButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    user = Client.apiService.registerUser(UserRegisterRequest(
                        name = nameText.text.toString(),
                        email = emailText.text.toString(),
                        password = passwordText.text.toString()
                    ))
                    Log.d("RegisterActivity", "User registered: ${user.name}")
                    Toast.makeText(this@RegisterActivity, "User registered successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    Log.e("RegisterActivity", "Error registering user: ${e.message}")
                    Toast.makeText(this@RegisterActivity, "Error registering user", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}