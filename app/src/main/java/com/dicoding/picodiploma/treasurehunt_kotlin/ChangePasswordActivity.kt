package com.dicoding.picodiploma.treasurehunt_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val emailInput = binding.emailInputChange
        val sendButton = binding.sendButtonChange

        emailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                sendButton.setBackgroundColor(ContextCompat.getColor(this@ChangePasswordActivity, R.color.dark_blue))
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendButton.setBackgroundColor(ContextCompat.getColor(this@ChangePasswordActivity, R.color.green))


                sendEmail()
            }

            override fun afterTextChanged(s: Editable?) {
                sendButton.setBackgroundColor(ContextCompat.getColor(this@ChangePasswordActivity, R.color.green))

                sendEmail()
            }

        })

        binding.backChange.setOnClickListener {
            val intentSplash = Intent(this, MainActivity::class.java)
            startActivity(intentSplash)
        }

    }

    private fun sendEmail() {
        val emailInput = binding.emailInputChange
        val sendButton = binding.sendButtonChange

        sendButton.setOnClickListener {
            if (emailInput.text.toString().isNotEmpty()) {
                Toast.makeText(this, "Email Terkirim!", Toast.LENGTH_SHORT).show()

            }
            else {
                Toast.makeText(this, "Masukkan Email!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}