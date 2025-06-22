package com.example.pulsum

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NotificationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        setupViews()
    }

    private fun setupViews() {
        // 뒤로가기 버튼
        val ivBack = findViewById<ImageView>(R.id.iv_back)
        ivBack.setOnClickListener {
            finish()
        }

        // 저장 버튼
        val ivCheck = findViewById<ImageView>(R.id.iv_check)
        ivCheck.setOnClickListener {
            Toast.makeText(this, "Notification settings saved", Toast.LENGTH_SHORT).show()
        }

        // 각 알림 항목의 토글 스위치 설정
        setupNotificationToggle(R.id.ll_water, R.id.iv_toggle_water, "water")
        setupNotificationToggle(R.id.ll_repot, R.id.iv_toggle_repot, "repot")
        setupNotificationToggle(R.id.ll_clean, R.id.iv_toggle_clean, "clean")
        setupNotificationToggle(R.id.ll_rotate, R.id.iv_toggle_rotate, "rotate")
        setupNotificationToggle(R.id.ll_fertilizer, R.id.iv_toggle_fertilizer, "fertilizer")
        setupNotificationToggle(R.id.ll_trim, R.id.iv_toggle_trim, "trim")
        setupNotificationToggle(R.id.ll_scan, R.id.iv_toggle_scan, "scan")
    }

    private fun setupNotificationToggle(containerId: Int, toggleId: Int, type: String) {
        val container = findViewById<LinearLayout>(containerId)
        val toggleButton = findViewById<ImageView>(toggleId)

        // SharedPreferences에서 저장된 상태 읽기
        val sharedPref = getSharedPreferences("notification_settings", MODE_PRIVATE)
        val isEnabled = sharedPref.getBoolean("${type}_enabled", true) // 기본값은 true

        // 초기 상태 설정
        updateToggleState(toggleButton, isEnabled)

        // 전체 컨테이너 클릭 시 토글
        container.setOnClickListener {
            val currentState = sharedPref.getBoolean("${type}_enabled", true)
            val newState = !currentState

            // 상태 저장
            sharedPref.edit().putBoolean("${type}_enabled", newState).apply()

            // UI 업데이트
            updateToggleState(toggleButton, newState)

            // 토스트 메시지
            val message = if (newState) "$type notifications enabled" else "$type notifications disabled"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        // 토글 버튼 직접 클릭 시에도 동작
        toggleButton.setOnClickListener {
            container.performClick()
        }
    }

    private fun updateToggleState(toggleButton: ImageView, isEnabled: Boolean) {
        if (isEnabled) {
            // 활성화 상태 - 초록색
            toggleButton.setImageResource(R.drawable.toggle_on)
            toggleButton.setColorFilter(getColor(R.color.pulsum_primary_green))
        } else {
            // 비활성화 상태 - 회색
            toggleButton.setImageResource(R.drawable.toggle_off)
            toggleButton.setColorFilter(getColor(android.R.color.darker_gray))
        }
    }
}