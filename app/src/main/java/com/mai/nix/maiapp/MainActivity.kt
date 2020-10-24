package com.mai.nix.maiapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.mai.nix.maiapp.navigation_fragments.*
import com.mai.nix.maiapp.navigation_fragments.campus.CampusTabsFragment
import com.mai.nix.maiapp.navigation_fragments.life.LifeTabFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //@JvmField
    //var subjectsNeedToUpdate = false

    //@JvmField
    //var examsNeedToUpdate = false
    private var selectedItemId = -999
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)

        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, mainToolbar, R.string.app_name,
                R.string.app_name) {
            override fun onDrawerClosed(drawerView: View) {
                chooseFragmentById()
                super.onDrawerClosed(drawerView)
            }
        }

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.navigation)
        navigationView.setNavigationItemSelectedListener { item ->
            selectedItemId = item.itemId
            drawerLayout.closeDrawers()
            true
        }

        if (savedInstanceState != null) {
            selectedItemId = savedInstanceState.getInt(SELECTED_ITEM_ID)
            chooseFragmentById()
        } else {
            supportActionBar?.setTitle(R.string.menu_schedule)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFragmentHost, ScheduleFragment())
                    .commit()
        }
    }

    private fun setFragment(title: String, fragment: Fragment) {
        supportActionBar?.title = title
        supportFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentHost, fragment)
                .commit()
    }

    private fun chooseFragmentById() {
        when (selectedItemId) {
            R.id.menu_sch -> setFragment("Расписание пар", ScheduleFragment())
            R.id.menu_sch_ex -> setFragment("Расписание сессии", ExamScheduleFragment())
            R.id.menu_campus -> setFragment("Кампус", CampusTabsFragment())
            R.id.life -> setFragment("Жизнь", LifeTabFragment())
            R.id.menu_settings -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivityForResult(intent, UPDATE_SCHEDULE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UPDATE_SCHEDULE) {
            //subjectsNeedToUpdate = true
            //examsNeedToUpdate = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt(SELECTED_ITEM_ID, selectedItemId)
    }

    companion object {
        const val UPDATE_SCHEDULE = 69
        private const val SELECTED_ITEM_ID = "selected_item_id"
    }
}