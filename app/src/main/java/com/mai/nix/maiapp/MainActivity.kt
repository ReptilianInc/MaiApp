package com.mai.nix.maiapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.mai.nix.maiapp.navigation_fragments.campus.CampusTabsFragment
import com.mai.nix.maiapp.navigation_fragments.exams.ExamScheduleFragment
import com.mai.nix.maiapp.navigation_fragments.life.LifeTabFragment
import com.mai.nix.maiapp.navigation_fragments.settings.SettingsFragment
import com.mai.nix.maiapp.navigation_fragments.subjects.ScheduleFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private var retainFragment: Fragment? = null
    private var currentTitle = "Расписание пар"

    private var selectedItemId = -999
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) currentTitle = savedInstanceState.getString(CURRENT_TOOLBAR_TITLE)?: ""
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

        retainFragment = supportFragmentManager.findFragmentByTag(CURRENT_FRAGMENT_TAG)
        if (retainFragment == null) {
            retainFragment = ScheduleFragment()
            supportFragmentManager.beginTransaction()
                    .add(R.id.mainFragmentHost, retainFragment!!, CURRENT_FRAGMENT_TAG)
                    .commit()
        }
        supportActionBar?.title = currentTitle
    }

    private fun setFragment(title: String, fragment: Fragment) {
        if (retainFragment!!::class == fragment::class) return
        currentTitle = title
        supportActionBar?.title = currentTitle

        supportFragmentManager.beginTransaction()
                .remove(retainFragment!!)
                .commit()

        retainFragment = fragment
        supportFragmentManager.beginTransaction()
                .add(R.id.mainFragmentHost, retainFragment!!, CURRENT_FRAGMENT_TAG)
                .commit()
    }

    private fun chooseFragmentById() {
        when (selectedItemId) {
            R.id.menu_sch -> setFragment("Расписание пар", ScheduleFragment())
            R.id.menu_sch_ex -> setFragment("Расписание сессии", ExamScheduleFragment())
            R.id.menu_campus -> setFragment("Кампус", CampusTabsFragment())
            R.id.life -> setFragment("Жизнь", LifeTabFragment())
            R.id.menu_settings -> setFragment("Настройки", SettingsFragment())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_ITEM_ID, selectedItemId)
        outState.putString(CURRENT_TOOLBAR_TITLE, currentTitle)
    }

    companion object {
        private const val SELECTED_ITEM_ID = "selected_item_id"
        private const val CURRENT_TOOLBAR_TITLE = "current_toolbar_title"
        private const val CURRENT_FRAGMENT_TAG = "current_fragment_tag"
    }
}