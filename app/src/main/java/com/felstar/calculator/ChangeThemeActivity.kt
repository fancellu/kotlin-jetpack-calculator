package com.felstar.calculator

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.felstar.calculator.databinding.ActivityChangeThemeBinding

class ChangeThemeActivity : AppCompatActivity() {

    lateinit var switchBinding : ActivityChangeThemeBinding
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        switchBinding = ActivityChangeThemeBinding.inflate(layoutInflater)
        val view = switchBinding.root
        setContentView(view)

        // when we click back
        switchBinding.toolbar2.setNavigationOnClickListener {
            println("Finish")
            finish()
        }

        switchBinding.mySwitch.setOnCheckedChangeListener { _, isChecked ->

            sharedPreferences = this.getSharedPreferences("Dark Theme", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("switch",true)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("switch",false)
            }

            editor.apply()

        }

    }

    override fun onResume() {
        super.onResume()

        sharedPreferences = this.getSharedPreferences("Dark Theme", MODE_PRIVATE)
        val isDark = sharedPreferences.getBoolean("switch",false)
        switchBinding.mySwitch.isChecked = isDark

    }
}