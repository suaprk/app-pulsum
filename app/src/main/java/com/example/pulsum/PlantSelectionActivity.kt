package com.example.pulsum

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlantSelectionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlantCategoryAdapter
    private lateinit var btnNext: Button
    private val selectedPlants = mutableListOf<PlantItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_selection)

        setupViews()
        setupRecyclerView()
    }

    private fun setupViews() {
        // 뒤로가기 버튼
        val ivBack = findViewById<ImageView>(R.id.iv_back)
        ivBack.setOnClickListener {
            finish()
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

        // NEXT 버튼
        btnNext = findViewById(R.id.btn_next)
        btnNext.setOnClickListener {
            if (selectedPlants.isNotEmpty()) {
                // 선택된 식물들과 함께 OfferActivity로 이동
                val intent = Intent(this, OfferActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select at least one plant", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view_plants)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        // 식물 카테고리 데이터 (기존 drawable 리소스 사용)
        val plantCategories = listOf(
            PlantCategory("Air-cleaning", listOf(
                PlantItem("Monstera", R.drawable.air_cleaning_1, false),        // 첫 번째 새 이미지
                PlantItem("Snake Plant", R.drawable.air_cleaning_2, false),      // 두 번째 새 이미지
                PlantItem("Peace Lily", R.drawable.air_cleaning_3, false)        // 세 번째 새 이미지
            )),
            PlantCategory("Cactus", listOf(
                PlantItem("Succulent", R.drawable.cactus_1, false),
                PlantItem("Barrel Cactus", R.drawable.cactus_2, false),
                PlantItem("Jade Plant", R.drawable.cactus_3, false)
            )),
            PlantCategory("Violet", listOf(
                PlantItem("African Violet", R.drawable.violet_1, false),
                PlantItem("Blue Violet", R.drawable.violet_2, false),
                PlantItem("White Violet", R.drawable.violet_3, false)
            )),
            PlantCategory("Crop", listOf(
                PlantItem("Rosemary", R.drawable.crop_1, false),
                PlantItem("Basil", R.drawable.crop_2, false),
                PlantItem("Mint", R.drawable.crop_3, false)
            ))
        )

        adapter = PlantCategoryAdapter(
            plantCategories,
            onPlantClick = { plantItem ->
                // 식물 아이템 클릭 시 관리법 화면으로 이동
                val intent = Intent(this, PlantCareActivity::class.java)
                intent.putExtra("plant_name", plantItem.name)
                intent.putExtra("plant_image", plantItem.imageRes)
                startActivity(intent)
            },
            onPlantSelect = { plantItem, isSelected ->
                // 체크박스 클릭 시 선택 상태 관리
                if (isSelected) {
                    selectedPlants.add(plantItem)
                } else {
                    selectedPlants.remove(plantItem)
                }
                updateNextButtonState()
            }
        )

        recyclerView.adapter = adapter
    }

    private fun updateNextButtonState() {
        // 선택된 식물이 있으면 버튼 활성화
        btnNext.isEnabled = selectedPlants.isNotEmpty()
        btnNext.alpha = if (selectedPlants.isNotEmpty()) 1.0f else 0.5f
    }
}

// 데이터 클래스들
data class PlantCategory(
    val name: String,
    val plants: List<PlantItem>
)

data class PlantItem(
    val name: String,
    val imageRes: Int,
    var isSelected: Boolean = false
)