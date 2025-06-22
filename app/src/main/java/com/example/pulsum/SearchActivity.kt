package com.example.pulsum

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // 뒤로가기 버튼
        val ivBack = findViewById<ImageView>(R.id.iv_back)
        ivBack.setOnClickListener {
            finish() // 액티비티 종료
        }

        // 검색창 포커스
        val searchEditText = findViewById<EditText>(R.id.et_search)
        searchEditText.requestFocus()

        // 검색어 입력 감지
        searchEditText.addTextChangedListener { text ->
            // 검색 로직 (나중에 구현)
        }
    }
}