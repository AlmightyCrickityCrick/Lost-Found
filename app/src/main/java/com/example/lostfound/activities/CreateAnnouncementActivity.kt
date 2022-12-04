package com.example.lostfound.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lostfound.databinding.ActivityCreateAnnouncementBinding
import com.example.lostfound.databinding.ActivityMainBinding

class CreateAnnouncementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAnnouncementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityCreateAnnouncementBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}