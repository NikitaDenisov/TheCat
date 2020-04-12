package com.denisov.cat.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.denisov.cat.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val catsFragment = CatsFragment.newInstance()
        val favoriteFragment = FavoritesFragment.newInstance()

        navigationView.setOnNavigationItemSelectedListener {
            when {
                it.itemId == R.id.cats && navigationView.selectedItemId != R.id.cats -> {
                    showFragment(catsFragment, CatsFragment.TAG)
                }
                it.itemId == R.id.favorites && navigationView.selectedItemId != R.id.favorites -> {
                    showFragment(favoriteFragment, FavoritesFragment.TAG)
                }
            }
            true
        }
        showFragment(catsFragment, CatsFragment.TAG)
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .apply {
                if (fragment.isAdded) {
                    show(fragment)
                } else {
                    add(R.id.fragmentContainer, fragment, tag)
                }
                supportFragmentManager
                    .fragments
                    .filter { it != fragment }
                    .forEach { hide(it) }
            }
            .commitNow()
    }
}