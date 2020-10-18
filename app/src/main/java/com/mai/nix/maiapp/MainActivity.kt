package com.mai.nix.maiapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.mai.nix.maiapp.navigation_fragments.*

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var fragment: Fragment
    private var toolbar: Toolbar? = null

    @JvmField
    var subjectsNeedToUpdate = false

    @JvmField
    var examsNeedToUpdate = false
    private var mSelectedItemId = -999
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.kek)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.menu_schedule)
        drawerLayout = findViewById(R.id.drawer_layout)
        fragment = ScheduleFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.center_view, fragment)
                .commit()
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_schedule,
                R.string.menu_schedule) {
            override fun onDrawerClosed(drawerView: View) {
                when (mSelectedItemId) {
                    R.id.menu_sch -> setFragment("Расписание пар", ScheduleFragment())
                    R.id.menu_sch_ex -> setFragment("Расписание сессии", ExamScheduleFragment())
                    R.id.press -> setFragment("Пресс-центр", PressCenterFragment())
                    R.id.menu_campus -> setFragment("Кампус", CampusFragment())
                    R.id.life -> setFragment("Жизнь", LifeFragment())
                    R.id.menu_settings -> {
                        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                        startActivityForResult(intent, UPDATE_SCHEDULE)
                    }
                }
                mSelectedItemId = -999
                super.onDrawerClosed(drawerView)
            }
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.navigation)
        navigationView.setNavigationItemSelectedListener { item ->
            mSelectedItemId = item.itemId
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun setFragment(title: String, fragment: Fragment) {
        toolbar!!.title = title
        this.fragment = fragment
        supportFragmentManager.beginTransaction()
                .replace(R.id.center_view, this.fragment)
                .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UPDATE_SCHEDULE) {
            //subjectsNeedToUpdate = true
            //examsNeedToUpdate = true
        }
    }

    companion object {
        const val UPDATE_SCHEDULE = 69
    }
}