package com.example.pulsum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.pulsum.R

class AddRecordsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_record, container, false)

        // ← 버튼 클릭 시 이전 화면으로 돌아가기
        val btnBack = view.findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}