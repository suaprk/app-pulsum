package com.example.pulsum

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ImageAddActivity : AppCompatActivity() {

    private lateinit var selectedImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_add)

        // Intent에서 이미지 URI 받기
        val imageUriString = intent.getStringExtra("image_uri")
        if (imageUriString != null) {
            selectedImageUri = Uri.parse(imageUriString)
            setupViews()
        } else {
            Toast.makeText(this, "이미지를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupViews() {
        val ivBack = findViewById<ImageView>(R.id.iv_back)
        val ivSelectedImage = findViewById<ImageView>(R.id.iv_selected_image)
        val btnAdd = findViewById<Button>(R.id.btn_add)
        val tvPlantName = findViewById<TextView>(R.id.tv_plant_name)
        val tvDescription = findViewById<TextView>(R.id.tv_description)

        // 뒤로가기 버튼
        ivBack.setOnClickListener {
            finish()
        }

        // 선택된 이미지 표시
        ivSelectedImage.setImageURI(selectedImageUri)

        // 식물 이름과 설명 설정
        tvPlantName.text = "Monstera"
        tvDescription.text = "Adds the selected image"

        // Add 버튼 클릭 이벤트
        btnAdd.setOnClickListener {
            addImageToGallery()
        }

        // 이미지 좌우 네비게이션
        findViewById<ImageView>(R.id.iv_prev).setOnClickListener {
            Toast.makeText(this, "이전 이미지", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageView>(R.id.iv_next).setOnClickListener {
            Toast.makeText(this, "다음 이미지", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addImageToGallery() {
        // SharedPreferences에 이미지 URI 저장
        val sharedPref = getSharedPreferences("gallery_images", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        // 기존 이미지 목록 가져오기
        val existingImages = sharedPref.getStringSet("image_uris", mutableSetOf()) ?: mutableSetOf()
        val imageList = existingImages.toMutableList()

        // 새 이미지를 맨 앞에 추가 (최신순)
        val imageUriString = selectedImageUri.toString()
        imageList.remove(imageUriString) // 중복 제거
        imageList.add(0, imageUriString) // 맨 앞에 추가

        // 최대 10개까지만 저장 (선택사항)
        if (imageList.size > 10) {
            imageList.removeAt(imageList.size - 1)
        }

        // SharedPreferences에 저장
        editor.putStringSet("image_uris", imageList.toSet())

        // 추가 정보도 저장 (날짜, 식물명 등)
        val currentTime = System.currentTimeMillis()
        editor.putLong("image_${imageUriString.hashCode()}_time", currentTime)
        editor.putString("image_${imageUriString.hashCode()}_plant", "Monstera")

        editor.apply()

        // 성공 메시지
        Toast.makeText(this, "Growth Gallery에 이미지가 추가되었습니다!", Toast.LENGTH_SHORT).show()

        // 결과를 설정하고 액티비티 종료
        setResult(RESULT_OK)
        finish()
    }
}