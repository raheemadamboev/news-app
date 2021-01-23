package xyz.teamgravity.newsapp.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import xyz.teamgravity.newsapp.R
import xyz.teamgravity.newsapp.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // find nav controller
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        binding.apply {
            // bottom navigation view
            bottomNavigationView.setupWithNavController(navController)

            // do not respond if it again selected
            bottomNavigationView.setOnNavigationItemReselectedListener { }

            // hide bottom navigation view
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id) {
                    R.id.articleFragment -> bottomNavigationView.visibility = View.GONE
                    else -> bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }
}