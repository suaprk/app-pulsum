package com.example.pulsum

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class MyPageFragment : Fragment() {

    private lateinit var tvUserName: TextView
    private lateinit var ivProfileImage: ImageView

    // Edit Profile 결과 처리
    private val editProfileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 프로필이 업데이트되었으므로 UI 새로고침
            loadProfileData()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)

        setupViews(view)
        loadProfileData() // 프로필 데이터 로드

        return view
    }

    private fun setupViews(view: View) {
        // 뷰 연결
        tvUserName = view.findViewById(R.id.tv_user_name)
        ivProfileImage = view.findViewById(R.id.iv_profile_image)

        // 뒤로가기 버튼
        val ivBack = view.findViewById<ImageView>(R.id.iv_back)
        ivBack.setOnClickListener {
            // 뒤로가기 기능 (필요시 구현)
        }

        // 체크 버튼 (저장 기능)
        val ivCheck = view.findViewById<ImageView>(R.id.iv_check)
        ivCheck.setOnClickListener {
            Toast.makeText(context, "Settings saved", Toast.LENGTH_SHORT).show()
        }

        // Edit Profile 버튼 - EditProfileActivity로 이동
        val tvEditProfile = view.findViewById<TextView>(R.id.tv_edit_profile)
        tvEditProfile.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            editProfileLauncher.launch(intent)
        }

        // Settings 메뉴들
        setupSettingsMenus(view)

        // Log Out 버튼
        val btnLogOut = view.findViewById<Button>(R.id.btn_log_out)
        btnLogOut.setOnClickListener {
            showLogOutDialog()
        }
    }

    private fun loadProfileData() {
        // SharedPreferences에서 저장된 프로필 데이터 로드
        val sharedPref = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)

        // 사용자 이름 업데이트
        val userName = sharedPref.getString("name", "Anita Rose")
        tvUserName.text = userName

        // 프로필 이미지 업데이트
        val savedImageUri = sharedPref.getString("profile_image_uri", null)
        if (savedImageUri != null) {
            try {
                ivProfileImage.setImageURI(Uri.parse(savedImageUri))
            } catch (e: Exception) {
                // 이미지 로드 실패 시 기본 이미지 사용
                ivProfileImage.setImageResource(R.drawable.profile_placeholder)
            }
        }
    }

    private fun setupSettingsMenus(view: View) {
        // Preferences
        val llPreferences = view.findViewById<LinearLayout>(R.id.ll_preferences)
        llPreferences.setOnClickListener {
            Toast.makeText(context, "Preferences clicked", Toast.LENGTH_SHORT).show()
        }

        // Notifications - NotificationsActivity로 이동
        val llNotifications = view.findViewById<LinearLayout>(R.id.ll_notifications)
        llNotifications.setOnClickListener {
            val intent = Intent(activity, NotificationsActivity::class.java)
            startActivity(intent)
        }

        // Privacy and Security
        val llPrivacy = view.findViewById<LinearLayout>(R.id.ll_privacy)
        llPrivacy.setOnClickListener {
            Toast.makeText(context, "Privacy and Security clicked", Toast.LENGTH_SHORT).show()
        }

        // About
        val llAbout = view.findViewById<LinearLayout>(R.id.ll_about)
        llAbout.setOnClickListener {
            Toast.makeText(context, "About clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLogOutDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_logout, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        // 다이얼로그 배경을 투명하게 설정
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // 버튼 클릭 이벤트 설정
        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            dialog.dismiss()
            performLogOut()
        }

        dialog.show()
    }

    private fun performLogOut() {
        // 로그아웃 로직 구현
        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()

        // 실제 앱에서는 여기서 로그인 화면으로 이동하거나
        // SharedPreferences에서 사용자 정보 삭제 등의 작업 수행
    }
}