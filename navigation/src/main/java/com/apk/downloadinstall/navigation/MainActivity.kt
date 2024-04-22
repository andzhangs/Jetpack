package com.apk.downloadinstall.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {

    private val mFragmentController by lazy {Navigation.findNavController(this, R.id.fragmentContainerView)}

    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //添加actionBar的Menu
        appBarConfiguration = AppBarConfiguration.Builder(mFragmentController.graph).build()

        NavigationUI.setupActionBarWithNavController(this,mFragmentController,appBarConfiguration)

        //监听页面切换跳转
        mFragmentController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.i(
                "print_logs",
                "onDestinationChanged: ${arguments.toString()}"
            )
        }
    }

    /**
     * 重写
     */
//    override fun onSupportNavigateUp(): Boolean {
//        return mFragmentController.navigateUp()
//    }

    /**
     * 重写
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_settings,menu)
        return true
    }

    /**
     * 重写
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,mFragmentController) || super.onOptionsItemSelected(item)
    }

    /**
     * 重写
     */
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mFragmentController,appBarConfiguration)
    }
}