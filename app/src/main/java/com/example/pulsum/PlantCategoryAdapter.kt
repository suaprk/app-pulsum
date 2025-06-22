package com.example.pulsum

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlantCategoryAdapter(
    private val categories: List<PlantCategory>,
    private val onPlantClick: (PlantItem) -> Unit,
    private val onPlantSelect: (PlantItem, Boolean) -> Unit
) : RecyclerView.Adapter<PlantCategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plant_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCategoryName: TextView = itemView.findViewById(R.id.tv_category_name)
        private val recyclerViewPlants: RecyclerView = itemView.findViewById(R.id.recycler_view_plants)

        fun bind(category: PlantCategory) {
            tvCategoryName.text = category.name

            // 식물 아이템들을 보여주는 RecyclerView 설정
            recyclerViewPlants.layoutManager = GridLayoutManager(itemView.context, 3)
            val plantAdapter = PlantItemAdapter(category.plants, onPlantClick, onPlantSelect)
            recyclerViewPlants.adapter = plantAdapter
        }
    }
}

class PlantItemAdapter(
    private val plants: List<PlantItem>,
    private val onPlantClick: (PlantItem) -> Unit,
    private val onPlantSelect: (PlantItem, Boolean) -> Unit
) : RecyclerView.Adapter<PlantItemAdapter.PlantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plant, parent, false)
        return PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.bind(plants[position])
    }

    override fun getItemCount(): Int = plants.size

    inner class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPlant: ImageView = itemView.findViewById(R.id.iv_plant)
        private val tvSelected: TextView = itemView.findViewById(R.id.iv_selected) // TextView로 변경

        fun bind(plant: PlantItem) {
            ivPlant.setImageResource(plant.imageRes)

            // 선택 상태에 따라 체크박스 표시 변경
            updateCheckboxState(plant.isSelected)

            // 이미지 클릭 시 관리법 화면으로 이동
            ivPlant.setOnClickListener {
                onPlantClick(plant)
            }

            // 체크박스 클릭 시 선택 상태 토글
            tvSelected.setOnClickListener {
                toggleSelection(plant)
            }

            // 전체 아이템 클릭 시 체크박스 토글
            itemView.setOnClickListener {
                toggleSelection(plant)
            }
        }

        private fun toggleSelection(plant: PlantItem) {
            plant.isSelected = !plant.isSelected
            onPlantSelect(plant, plant.isSelected)
            updateCheckboxState(plant.isSelected)
        }

        private fun updateCheckboxState(isSelected: Boolean) {
            if (isSelected) {
                tvSelected.setBackgroundColor(itemView.context.getColor(R.color.pulsum_primary_green))
                tvSelected.setTextColor(itemView.context.getColor(R.color.white))
                tvSelected.text = "✓"
            } else {
                tvSelected.setBackgroundColor(itemView.context.getColor(R.color.white))
                tvSelected.setTextColor(itemView.context.getColor(android.R.color.darker_gray))
                tvSelected.text = ""
            }
        }
    }
}