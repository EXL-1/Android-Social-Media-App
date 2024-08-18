package com.example.a22806287

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.util.Calendar

// Main Activity Class
var calendar: Calendar = Calendar.getInstance()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolBar : Toolbar = findViewById(R.id.toolbar)
        val loginFragment = LoginFragment()

        // Code For Toolbar
        setSupportActionBar(toolBar)
        supportActionBar!!.title = "Start Menu"

        Global.loadFragment(supportFragmentManager, R.id.flFragment, loginFragment)

        val sp: SharedPreferences = getSharedPreferences("Application_info", Context.MODE_PRIVATE)
        val editor = sp.edit();

        // Detects whether the user was previously logged in or not.
        val isLoggedIn = sp.getBoolean("isLoggedIn", false)

        editor.apply()

        if (isLoggedIn) {
            Intent(this, NavDrawerActivity::class.java).also {
                startActivity(it)
            }
        }
    }

}