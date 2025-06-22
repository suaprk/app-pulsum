package com.example.pulsum

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottom_navigation)

        // 첫 화면을 Home으로 설정
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        // 하단 네비게이션 클릭 리스너
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_records -> {
                    loadFragment(RecordsFragment())
                    true
                }
                R.id.nav_care -> {
                    loadFragment(CareFragment())
                    true
                }
                R.id.nav_my -> {
                    loadFragment(MyPageFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}