package com.securemsg.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.securemsg.databinding.ActivitySignupBinding
import java.security.SecureRandom
import android.util.Base64
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    interface Api {
        @POST("/register")
        suspend fun register(@Body body: Map<String, String>) : Map<String, Any>
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(Api::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = generateToken()
        binding.tokenView.text = token

        binding.createAccountBtn.setOnClickListener {
            val username = binding.usernameField.text.toString().trim()
            CoroutineScope(Dispatchers.IO).launch {
                api.register(mapOf("username" to username, "token" to token))
            }
        }
    }

    private fun generateToken(): String {
        val rnd = SecureRandom()
        val bytes = ByteArray(32)
        rnd.nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}
