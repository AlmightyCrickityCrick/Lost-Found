package com.example.lostfound.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.lostfound.R
import com.example.lostfound.data.model.LoggedInUser
import com.example.lostfound.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navDrawer: NavigationView
    private lateinit var mDrawer:DrawerLayout
    private val USER_CONST = "USER"
    lateinit var user : LoggedInUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if(result.resultCode == Activity.RESULT_OK){
                 user = result.data?.getSerializableExtra(USER_CONST) as LoggedInUser
            }
        }
        var login = Intent(this, LoginActivity::class.java)
        resultLauncher.launch(login)

        mDrawer =  binding.drawerLayout
        navDrawer = binding.nvDrawer
        setUpDrawerContent(navDrawer)
        navDrawer.checkedItem?.let { selectDrawerItem(it) }
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
            R.id.nav_dashboard -> {fragment = Dashboard()
            }
            R.id.nav_chat -> {fragment = ChatList()
            }
        }
        var fragmentM = supportFragmentManager
        if (fragment != null) {
            fragmentM.beginTransaction().replace(R.id.activity_content, fragment).commit()
        }
        menuItem.setChecked(true)
        mDrawer.closeDrawers()
    }
}