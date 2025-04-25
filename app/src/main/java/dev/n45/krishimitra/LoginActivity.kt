package dev.n45.krishimitra

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.n45.krishimitra.api.Client
import dev.n45.krishimitra.api.data.TokenResponse
import kotlinx.coroutines.launch
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import dev.n45.krishimitra.api.data.LoginRequest
import dev.n45.krishimitra.api.data.User

class LoginActivity : AppCompatActivity() {
    private lateinit var tokenData: TokenResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailText = findViewById<EditText>(R.id.email)
        val passwordText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)
        val registerButton = findViewById<Button>(R.id.register_button)

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginButton.setOnClickListener {
            val email = emailText.text.toString()
            val password = passwordText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                val toast = Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT)
                toast.show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    tokenData = getToken(LoginRequest(
                        email = email,
                        password = password
                    ))
                    val user = getUser(tokenData.token)
                    val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
                    sharedPreferences.edit {
                        putString("access_token", tokenData.token)
                        putString("user_name", user.name)
                        putString("user_email", user.email)
                    }
                    Log.d("LoginActivity", "Token: ${tokenData.token}")
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    val toast = Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT)
                    toast.show()
                    finish()
                } catch (e: Exception) {
                    Log.e("LoginActivity", "Error logging in: ${e.message}")
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun getToken(loginRequest: LoginRequest): TokenResponse {
        return Client.apiService.loginUser(loginRequest)
    }

    private suspend fun getUser(token: String): User {
        return Client.apiService.getUser("Bearer $token")
    }
}