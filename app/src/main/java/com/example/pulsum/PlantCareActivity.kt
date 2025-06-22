package com.example.pulsum

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlantCareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_care)

        setupViews()
        loadPlantData()
    }

    private fun setupViews() {
        // 뒤로가기 버튼
        val ivBack = findViewById<ImageView>(R.id.iv_back)
        ivBack.setOnClickListener {
            finish() // PlantSelectionActivity로 돌아감
        }

        // 공유 버튼
        val ivShare = findViewById<ImageView>(R.id.iv_share)
        ivShare.setOnClickListener {
            // 공유 기능 구현 (선택사항)
        }

        // 편집 버튼
        val ivEdit = findViewById<ImageView>(R.id.iv_edit)
        ivEdit.setOnClickListener {
            // 편집 기능 구현 (선택사항)
        }
    }

    private fun loadPlantData() {
        // Intent에서 식물 정보 받기
        val plantName = intent.getStringExtra("plant_name") ?: "Monstera"
        val plantImage = intent.getIntExtra("plant_image", R.drawable.monstera)

        // 뷰에 데이터 설정
        val ivPlantImage = findViewById<ImageView>(R.id.iv_plant_image)
        val tvPlantTitle = findViewById<TextView>(R.id.tv_plant_title)
        val tvPlantScientific = findViewById<TextView>(R.id.tv_plant_scientific)
        val tvPlantOrigin = findViewById<TextView>(R.id.tv_plant_origin)
        val tvPlantType = findViewById<TextView>(R.id.tv_plant_type)

        ivPlantImage.setImageResource(plantImage)
        tvPlantTitle.text = "How To Care $plantName"

        // 식물별 정보 설정
        when (plantName) {
            "Monstera" -> {
                tvPlantScientific.text = "Monstera deliciosa"
                tvPlantOrigin.text = "Central America"
                tvPlantType.text = "Air-cleaning"
            }
            "Succulent" -> {
                tvPlantScientific.text = "Succulent species"
                tvPlantOrigin.text = "Desert regions"
                tvPlantType.text = "Cactus"
            }
            else -> {
                tvPlantScientific.text = plantName
                tvPlantOrigin.text = "Various regions"
                tvPlantType.text = "Indoor plant"
            }
        }
    }
}