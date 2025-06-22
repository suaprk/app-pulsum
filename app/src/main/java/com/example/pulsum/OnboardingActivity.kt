package com.example.pulsum

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var btnNext: Button
    private lateinit var adapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        setupViews()
        setupViewPager()
    }

    private fun setupViews() {
        viewPager = findViewById(R.id.viewPager)
        btnNext = findViewById(R.id.btn_next)

        btnNext.setOnClickListener {
            if (viewPager.currentItem < 1) { // 0에서 1로 (총 2페이지)
                // 다음 페이지로 이동
                viewPager.currentItem = viewPager.currentItem + 1
            } else {
                // 마지막 페이지에서 식물 선택 화면으로 이동
                startPlantSelectionActivity()
            }
        }
    }

    private fun setupViewPager() {
        adapter = OnboardingAdapter()
        viewPager.adapter = adapter

        // 페이지 변경 리스너
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 마지막 페이지에서 버튼 텍스트 변경 (선택사항)
                // btnNext.text = if (position == 1) "GET STARTED" else "NEXT"
            }
        })
    }

    private fun startPlantSelectionActivity() {
        // 온보딩 완료 상태 저장
        val sharedPref = getSharedPreferences("app_preferences", MODE_PRIVATE)
        sharedPref.edit().putBoolean("onboarding_completed", true).apply()

        // 식물 선택 화면으로 이동
        val intent = Intent(this, PlantSelectionActivity::class.java)
        startActivity(intent)
        finish()
    }
}