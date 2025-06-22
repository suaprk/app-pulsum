package com.example.pulsum

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class OnboardingAdapter : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    // onboarding_page_1 제거, 2개만 사용
    private val layouts = listOf(
        R.layout.onboarding_page_2,
        R.layout.onboarding_page_3
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layouts[viewType], parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        // 각 페이지별 특별한 설정이 필요하다면 여기서 처리
    }

    override fun getItemCount(): Int = layouts.size

    override fun getItemViewType(position: Int): Int = position

    class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}