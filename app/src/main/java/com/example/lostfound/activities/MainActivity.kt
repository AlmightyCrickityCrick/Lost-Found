package com.example.lostfound.activities

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.lostfound.R
import com.example.lostfound.data.model.LoggedInUser
import com.example.lostfound.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navDrawer: NavigationView
    lateinit var mDrawer:DrawerLayout
    private val USER_CONST = "USER"
    lateinit var user : LoggedInUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!checkPermission()) {
            requestPermission()
        }
//        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
//            if(result.resultCode == Activity.RESULT_OK){
//                 user = result.data?.getSerializableExtra(USER_CONST) as LoggedInUser
//            }
//        }
//        var login = Intent(this, LoginActivity::class.java)
//        resultLauncher.launch(login)
        user = intent.getSerializableExtra("USER") as LoggedInUser
        mDrawer =  binding.drawerLayout
        navDrawer = binding.nvDrawer
        var head = navDrawer.getHeaderView(0)
        var name= head.findViewById<TextView>(R.id.drawer_username)
        name.text = user.displayName
        var email = head.findViewById<TextView>(R.id.drawer_email)
        email.text = user.email
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
            R.id.nav_map ->{fragment = MapsFragment() }
            R.id.nav_profile->{fragment = ProfileFragment()}
        }
        var fragmentM = supportFragmentManager
        if (fragment != null) {
            fragmentM.beginTransaction().replace(R.id.activity_content, fragment).commit()
        }
        menuItem.setChecked(true)
        mDrawer.closeDrawers()
    }
    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
        val result1 = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
    }
}