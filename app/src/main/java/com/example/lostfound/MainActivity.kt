package com.example.lostfound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.lostfound.databinding.ActivityMainBinding
import com.example.lostfound.ui.login.LoginActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navDrawer: NavigationView
    private lateinit var mDrawer:DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var login = Intent(this, LoginActivity::class.java)
        startActivity(login)

        mDrawer =  binding.drawerLayout
        navDrawer = binding.nvDrawer
        setUpDrawerContent(navDrawer)
    }

    private fun setUpDrawerContent(navigationView: NavigationView ){
        navigationView.setNavigationItemSelectedListener {
            selectDrawerItem(it)
            true
        }
    }

    fun selectDrawerItem(menuItem: MenuItem){
        var fragment: Fragment? = null
        when(menuItem.itemId){
            R.id.nav_dashboard -> {fragment = Lost()}
            R.id.nav_chat -> {fragment = ChatList()}
        }
        var fragmentM = supportFragmentManager
        if (fragment != null) {
            fragmentM.beginTransaction().replace(R.id.activity_content, fragment).commit()
        }
        menuItem.setChecked(true)
        mDrawer.closeDrawers()
    }
}