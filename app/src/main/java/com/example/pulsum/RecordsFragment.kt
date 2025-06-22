package com.example.pulsum

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import com.example.pulsum.R

class RecordsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_records, container, false)

        // "New Record" 버튼 연결
        val btnNewRecord = view.findViewById<Button>(R.id.btn_new_record)
        btnNewRecord.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddRecordsFragment())
                .addToBackStack(null)
                .commit()
        }

        // CalendarView 날짜 클릭 이벤트
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            Log.d("RecordsFragment", "선택한 날짜: $selectedDate")

            // TODO: 여기서 해당 날짜의 기록을 필터링하여 표시할 수 있음
        }

        return view
    }
}