package com.example.pulsum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class CareFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = TextView(context)
        view.text = "Care 화면입니다\n식물 관리 일정과 환경 정보를 제공합니다"
        view.textSize = 20f
        return view
    }
}