package com.example.pulsum

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPhone: EditText

    private var selectedImageUri: Uri? = null

    // 갤러리에서 이미지 선택
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            imageUri?.let {
                selectedImageUri = it
                profileImage.setImageURI(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        setupViews()
        loadProfileData()
    }

    private fun setupViews() {
        // 뷰 연결
        profileImage = findViewById(R.id.iv_profile_image)
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        etPhone = findViewById(R.id.et_phone)

        // 뒤로가기 버튼
        val ivBack = findViewById<ImageView>(R.id.iv_back)
        ivBack.setOnClickListener {
            finish()
        }

        // 저장 버튼 (체크 아이콘)
        val ivSave = findViewById<ImageView>(R.id.iv_save)
        ivSave.setOnClickListener {
            showSaveConfirmDialog()
        }

        // Select Photo 버튼
        val tvSelectPhoto = findViewById<TextView>(R.id.tv_select_photo)
        tvSelectPhoto.setOnClickListener {
            openGallery()
        }

        // 프로필 이미지 클릭 시에도 갤러리 열기
        profileImage.setOnClickListener {
            openGallery()
        }
    }

    private fun loadProfileData() {
        // SharedPreferences에서 저장된 프로필 데이터 로드
        val sharedPref = getSharedPreferences("user_profile", Context.MODE_PRIVATE)

        etName.setText(sharedPref.getString("name", "Anita Rose"))
        etEmail.setText(sharedPref.getString("email", "Rosie123@gmail.com"))
        etUsername.setText(sharedPref.getString("username", "Rosie"))
        etPassword.setText(sharedPref.getString("password", "xxxxxxxxxx"))
        etPhone.setText(sharedPref.getString("phone", "010xxxxxxxx"))

        // 저장된 프로필 이미지가 있다면 로드
        val savedImageUri = sharedPref.getString("profile_image_uri", null)
        if (savedImageUri != null) {
            selectedImageUri = Uri.parse(savedImageUri)
            profileImage.setImageURI(selectedImageUri)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun showSaveConfirmDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_save_profile, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // 버튼 클릭 이벤트
        dialogView.findViewById<TextView>(R.id.btn_confirm).setOnClickListener {
            dialog.dismiss()
            saveProfileData()
        }

        dialogView.findViewById<TextView>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun saveProfileData() {
        // SharedPreferences에 데이터 저장
        val sharedPref = getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putString("name", etName.text.toString())
        editor.putString("email", etEmail.text.toString())
        editor.putString("username", etUsername.text.toString())
        editor.putString("password", etPassword.text.toString())
        editor.putString("phone", etPhone.text.toString())

        // 프로필 이미지 URI 저장
        selectedImageUri?.let {
            editor.putString("profile_image_uri", it.toString())
        }

        editor.apply()

        // 성공 메시지
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

        // 결과 반환하고 액티비티 종료
        setResult(RESULT_OK)
        finish()
    }
}