package com.kirwa.recipes

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.kirwa.recipes.adapter.ViewPagerAdapter
import com.kirwa.recipes.ui.breakfast.BreakfastFragment
import com.kirwa.recipes.ui.chicken.HomeFragment
import com.kirwa.recipes.ui.brunch.BrunchFragment
import com.kirwa.recipes.ui.dinner.DinnerFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var tableLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        viewPager = findViewById(R.id.viewpager) as ViewPager
        setupViewPager(viewPager!!)
        tableLayout = findViewById(R.id.tabs) as TabLayout
        tableLayout!!.setupWithViewPager(viewPager)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
//        val navController = findNavController(R.id.nav_host_fragment)
//         Passing each menu ID as a set of Ids because each
//         menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(HomeFragment(), "Chicken")
        viewPagerAdapter.addFragment(BreakfastFragment(), "Breakfast")
        viewPagerAdapter.addFragment(BrunchFragment(), "Brunch")
        viewPagerAdapter.addFragment(DinnerFragment(), "Dinner")
        viewPager.adapter = viewPagerAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
}