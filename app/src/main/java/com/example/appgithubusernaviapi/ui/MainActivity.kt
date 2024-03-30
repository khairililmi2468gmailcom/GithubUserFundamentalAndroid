package com.example.appgithubusernaviapi.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.appgithubusernaviapi.R
import com.example.appgithubusernaviapi.databinding.ActivityMainBinding
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appgithubusernaviapi.adapter.UsrAdap
import com.example.appgithubusernaviapi.model.Items
import com.example.appgithubusernaviapi.ui.favor.FavoriteActivity
import com.example.appgithubusernaviapi.ui.setting.ActivitySetting
import com.example.appgithubusernaviapi.ui.setting.SettingFactory
import com.example.appgithubusernaviapi.ui.setting.SettingPreference
import com.example.appgithubusernaviapi.ui.setting.dataStore
import com.example.appgithubusernaviapi.viewModel.SearchUserModel
import com.example.appgithubusernaviapi.viewModel.SettingModel
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    private val viewModel: SearchUserModel by viewModels()
    private val adapter = UsrAdap()
    private lateinit var binding: ActivityMainBinding
    private lateinit var settingViewModel: SettingModel
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val pref = SettingPreference.getInstance(application.dataStore)
        settingViewModel = ViewModelProvider(this, SettingFactory(pref)).get(SettingModel::class.java)
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            applyTheme(isDarkModeActive)
            updateMenuIcons(isDarkModeActive)
        }
        showViewModel()
        showRecycle()
        viewModel.isLoadingTake.observe(this, this::showLoading)
        viewModel.searchUser("q")
        binding.tvNotFound.visibility = View.GONE
        val searchView = binding.search
        configureSearchView(searchView)

    }
    private fun applyTheme(isDarkModeActive: Boolean) {
        if (isDarkModeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
    private fun updateMenuIcons(isDarkModeActive: Boolean) {
        val menu = binding.toolbar.menu
        val favoriteMenuItem = menu.findItem(R.id.favorite_btn)
        val settingMenuItem = menu.findItem(R.id.setting_btn)

        if (isDarkModeActive) {
            favoriteMenuItem.setIcon(R.drawable.favorite_white)
            settingMenuItem.setIcon(R.drawable.setting_white)
        } else {
            favoriteMenuItem.setIcon(R.drawable.baseline_favorite_24)
            settingMenuItem.setIcon(R.drawable.baseline_settings_24)
        }
    }

    private fun configureSearchView(searchView: SearchView) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
    private fun showViewModel() {
        viewModel.listGetSearch.observe(this) { searchList ->
            if (searchList.size != 0) {
                binding.tvNotFound.visibility = View.GONE
                binding.rvUser.visibility = View.VISIBLE
                adapter.setData(searchList)
            } else {
                binding.tvNotFound.visibility = View.VISIBLE
                binding.rvUser.visibility = View.GONE
                Toast.makeText(this, "User Not Found!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun showRecycle() {
        binding.rvUser.layoutManager =
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(this, 2)
            } else {
                LinearLayoutManager(this)
            }

        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback { data -> selectUsr(data) }
    }
    private fun selectUsr(user: Items) {
        Toast.makeText(this, "You choose ${user.login}", Toast.LENGTH_SHORT).show()
        val i = Intent(this, DetailUser::class.java)
        i.putExtra(DetailUser.EXTRA_ID, user.id)
        i.putExtra(DetailUser.EXTRA_USER, user.login)
        i.putExtra(DetailUser.EXTRA_AVATAR, user.avatarUrl)
        startActivity(i)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite_btn -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.setting_btn -> {
                val intent = Intent(this, ActivitySetting::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
