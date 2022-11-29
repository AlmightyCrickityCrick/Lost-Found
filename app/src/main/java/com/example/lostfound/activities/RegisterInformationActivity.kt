package com.example.lostfound.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lostfound.databinding.ActivityRegisterBinding
import com.example.lostfound.databinding.ActivityRegisterInformationBinding

class RegisterInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}