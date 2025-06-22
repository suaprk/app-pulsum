package com.example.pulsum

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class OfferActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer)

        setupViews()
    }

    private fun setupViews() {
        // 4$ → Free 버튼
        val btnFree = findViewById<Button>(R.id.btn_free)
        btnFree.setOnClickListener {
            // 혜택을 수락하고 메인 화면으로 이동
            goToMainActivity()
        }

        // Refuse offer 버튼
        val btnRefuse = findViewById<Button>(R.id.btn_refuse)
        btnRefuse.setOnClickListener {
            // 혜택을 거절하고 메인 화면으로 이동
            goToMainActivity()
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}