package com.example.pulsum

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlantDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_detail)

        // Intent에서 데이터 받기
        val plantName = intent.getStringExtra("plant_name") ?: "Unknown Plant"
        val scientificName = intent.getStringExtra("plant_scientific_name") ?: plantName
        val plantImage = intent.getIntExtra("plant_image", R.drawable.iris)
        val plantDate = intent.getStringExtra("plant_date") ?: "2025.9.16"
        val plantStatus = intent.getStringExtra("plant_status") ?: "Healthy"
        val lastCare = intent.getStringExtra("plant_last_care") ?: "4 Days"
        val nextTask = intent.getStringExtra("plant_next_task") ?: "Water"

        setupViews(plantName, scientificName, plantImage, plantDate, plantStatus, lastCare, nextTask)
    }

    private fun setupViews(
        plantName: String,
        scientificName: String,
        plantImage: Int,
        plantDate: String,
        plantStatus: String,
        lastCare: String,
        nextTask: String
    ) {
        // 뷰 연결
        val ivBack = findViewById<ImageView>(R.id.iv_back)
        val ivPlantImage = findViewById<ImageView>(R.id.iv_plant_image)
        val tvPlantName = findViewById<TextView>(R.id.tv_plant_name)
        val tvScientificName = findViewById<TextView>(R.id.tv_scientific_name)
        val tvPlantDate = findViewById<TextView>(R.id.tv_plant_date)
        val tvCurrentStatus = findViewById<TextView>(R.id.tv_current_status)
        val tvLastCare = findViewById<TextView>(R.id.tv_last_care)
        val tvNextTask = findViewById<TextView>(R.id.tv_next_task)

        // 데이터 설정
        tvPlantName.text = plantName
        tvPlantDate.text = "Added: $plantDate"
        ivPlantImage.setImageResource(plantImage)

        // 식물별 학명 설정
        tvScientificName.text = when (plantName.lowercase()) {
            "iris" -> "Iris"  // Iris는 학명도 "Iris"
            "monstera" -> "Monstera Deliciosa"
            "succulent" -> "Succulent"
            "rosemary" -> "Rosmarinus officinalis"
            else -> plantName
        }

        // 상태 정보 설정
        tvCurrentStatus.text = plantStatus
        tvLastCare.text = lastCare
        tvNextTask.text = nextTask

        // 뒤로가기 버튼
        ivBack.setOnClickListener {
            finish()
        }

        // 하단 월별 갤러리 이미지 설정
        setupMonthlyGallery(plantName, plantImage)

        // 식물별 색상 테마 적용
        applyPlantTheme(plantName)
    }

    private fun setupMonthlyGallery(plantName: String, plantImage: Int) {
        // 월별 갤러리 이미지들 설정
        val galleryImage1 = findViewById<ImageView>(R.id.iv_gallery_1)
        val galleryImage2 = findViewById<ImageView>(R.id.iv_gallery_2)
        val galleryImage3 = findViewById<ImageView>(R.id.iv_gallery_3)

        // 각 식물별로 해당 식물의 이미지 사용
        galleryImage1?.setImageResource(plantImage)
        galleryImage2?.setImageResource(plantImage)
        galleryImage3?.setImageResource(plantImage)
    }

    private fun applyPlantTheme(plantName: String) {
        val tvPlantName = findViewById<TextView>(R.id.tv_plant_name)

        when (plantName.lowercase()) {
            "iris" -> {
                // Iris는 보라색 테마
                tvPlantName.setTextColor(getColor(android.R.color.holo_purple))
            }
            "monstera" -> {
                // Monstera는 초록색 테마
                tvPlantName.setTextColor(getColor(R.color.pulsum_primary_green))
            }
            "succulent" -> {
                // Succulent는 청록색 테마
                tvPlantName.setTextColor(getColor(android.R.color.holo_blue_light))
            }
            "rosemary" -> {
                // Rosemary는 올리브색 테마
                tvPlantName.setTextColor(getColor(android.R.color.darker_gray))
            }
            else -> {
                // 기본 테마
                tvPlantName.setTextColor(getColor(R.color.pulsum_primary_green))
            }
        }
    }
}