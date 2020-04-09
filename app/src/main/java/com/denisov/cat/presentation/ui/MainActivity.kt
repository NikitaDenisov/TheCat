package com.denisov.cat.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.denisov.cat.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationView.setOnNavigationItemSelectedListener {
            when {
                it.itemId == R.id.cats && navigationView.selectedItemId != R.id.cats -> {
                    showFragment(CatsFragment.newInstance())
                }
                it.itemId == R.id.favorites && navigationView.selectedItemId != R.id.favorites -> {
                    showFragment(FavoritesFragment.newInstance())
                }
            }
            true
        }
        showFragment(CatsFragment.newInstance())
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commitNow()
    }
}