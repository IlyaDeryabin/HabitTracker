package ru.d3rvich.habittracker.presenter

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import ru.d3rvich.habittracker.R
import ru.d3rvich.habittracker.appComponent
import ru.d3rvich.habittracker.databinding.ActivityMainBinding
import ru.d3rvich.habittracker.di.FeatureComponent

class MainActivity : AppCompatActivity() {

    internal val featureComponent: FeatureComponent by lazy {
        applicationContext.appComponent.featureComponent().build()
    }

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavViewHeader()
        binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        binding.drawer.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        })
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.habit_list_fragment, R.id.about_fragment),
            binding.drawer
        )
        binding.navView.setupWithNavController(navController)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.navView.setCheckedItem(R.id.habit_list_fragment)
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            menuItem.onNavDestinationSelected(navController)
            binding.drawer.close()
            true
        }
    }

    private fun setupNavViewHeader() {
        val shimmer = Shimmer.AlphaHighlightBuilder().apply {
            setDuration(1000)
            setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            setBaseAlpha(0.7f)
            setHighlightAlpha(0.6f)
            setAutoStart(true)
        }.build()
        val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(shimmer)
        }

        val headerView = binding.navView.inflateHeaderView(R.layout.drawer_header)
        val imageView = headerView.findViewById<ImageView>(R.id.image_view)
        Glide.with(this)
            .load("https://disgustingmen.com/wp-content/uploads/2017/11/dirty-thunder-3.jpg")
            .placeholder(shimmerDrawable)
            .error(R.drawable.ic_baseline_error_24)
            .centerCrop()
            .circleCrop()
            .into(imageView)
    }
}