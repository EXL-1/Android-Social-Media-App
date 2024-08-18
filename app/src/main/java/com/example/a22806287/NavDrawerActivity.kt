package com.example.a22806287

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.a22806287.post.PostsFragment
import com.example.a22806287.student.EditStudentFragment
import com.example.a22806287.student.StudentsFragment
import com.google.android.material.navigation.NavigationView

// NavDrawerActivity
class NavDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        // Defining Values / Shared Preferences / Fragments
        val sp: SharedPreferences = getSharedPreferences("Application_info", Context.MODE_PRIVATE)
        val studentsFragment = StudentsFragment()
        val postsFragment = PostsFragment()

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val navDrawerEmail = headerView.findViewById<TextView>(R.id.nav_drawer_email)
        val navDrawerGroup = headerView.findViewById<TextView>(R.id.nav_drawer_group)

        // Setting the Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val email = sp.getString("userEmail", "")
        val group = sp.getString("group", "")

        // Tailored to varying Groups
        if (group == "Admin") {
            supportActionBar!!.title = "Admin Portal"
            Global.loadFragment(supportFragmentManager, R.id.flfragment_nav_drawer, studentsFragment)
        } else {
            supportActionBar!!.title = "Student Portal"
            Global.loadFragment(supportFragmentManager, R.id.flfragment_nav_drawer, postsFragment)
        }

        navDrawerEmail.text = email
        navDrawerGroup.text = group
    }

    // Item Buttons for Navigation Drawer Menu
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.nav_home -> home()
            R.id.nav_profile -> profile()
            R.id.nav_logout -> logout()
        }
        
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    // Loads the Home menu pages for admins or students
    private fun home() {
        val studentsFragment = StudentsFragment()
        val postsFragment = PostsFragment()
        val sp: SharedPreferences = getSharedPreferences("Application_info", Context.MODE_PRIVATE)

        val group = sp.getString("group", "")

        if (group == "Admin") {
            Global.loadFragment(supportFragmentManager, R.id.flfragment_nav_drawer, studentsFragment)
        } else {
            Global.loadFragment(supportFragmentManager, R.id.flfragment_nav_drawer, postsFragment)
        }
    }
    // Loads the Students Profile
    private fun profile() {

        val editStudentFragment = EditStudentFragment()
        val sp: SharedPreferences = getSharedPreferences("Application_info", Context.MODE_PRIVATE)
        //val editor = sp.edit();

        val group = sp.getString("group", "")

        if (group == "Admin") {
            Toast.makeText(this , "Admins cannot change their profiles!", Toast.LENGTH_SHORT).show()
        } else {
            Global.loadFragment(supportFragmentManager, R.id.flfragment_nav_drawer, editStudentFragment)
        }
    }

    // Logs out the Student/Admin Logged in
    private fun logout() {

        val sp: SharedPreferences = getSharedPreferences("Application_info", Context.MODE_PRIVATE)
        val editor = sp.edit();

        editor.clear()
        editor.apply()

        Toast.makeText(this , "Successfully Logged out!", Toast.LENGTH_SHORT).show()

        Intent(this, MainActivity::class.java).also {
            startActivity(it)
        }
    }



}