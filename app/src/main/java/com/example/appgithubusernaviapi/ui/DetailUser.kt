package com.example.appgithubusernaviapi.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.appgithubusernaviapi.R
import com.example.appgithubusernaviapi.adapter.SPAdapter
import com.example.appgithubusernaviapi.databinding.DetailActivityBinding
import com.example.appgithubusernaviapi.ui.setting.SettingFactory
import com.example.appgithubusernaviapi.ui.setting.SettingPreference
import com.example.appgithubusernaviapi.ui.setting.dataStore
import com.example.appgithubusernaviapi.viewModel.SettingModel
import com.example.appgithubusernaviapi.viewModel.UserDetailModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUser : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"
        var username = String()
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }

    private val viewModel: UserDetailModel by viewModels()
    private lateinit var binding: DetailActivityBinding
    private lateinit var settingViewModel: SettingModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreference.getInstance(application.dataStore)
        settingViewModel = ViewModelProvider(this, SettingFactory(pref)).get(SettingModel::class.java)

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            applyTheme(isDarkModeActive)
            updateTextColor(isDarkModeActive)
            updateBackground(isDarkModeActive)
        }
        setupUserDetail()
    }

    private fun applyTheme(isDarkModeActive: Boolean) {
        if (isDarkModeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
    private fun updateBackground(isDarkModeActive: Boolean) {
        val backgroundColor = if (isDarkModeActive) {
            getColor(R.color.black)
        } else {
            getColor(R.color.white)
        }
        binding.constraintLayout.setBackgroundColor(backgroundColor)
    }

    private fun updateTextColor(isDarkModeActive: Boolean) {
        val textColor = if (isDarkModeActive) {
            getColor(android.R.color.white) // Change to your desired text color in dark mode
        } else {
            getColor(android.R.color.black) // Change to your desired text color in light mode
        }
        binding.tvName.setTextColor(textColor)
        binding.tvUsername.setTextColor(textColor)
        binding.tvCompany.setTextColor(textColor)
        binding.tvLocation.setTextColor(textColor)
        binding.tvRepository.setTextColor(textColor)
        binding.tvRepositoryValue.setTextColor(textColor)
        binding.tvFollowersValue.setTextColor(textColor)
        binding.tvFollowingValue.setTextColor(textColor)
        binding.tvFollowers.setTextColor(textColor)
        binding.tvFollowing.setTextColor(textColor)
    }

    private fun setupUserDetail() {
        supportActionBar?.title = resources.getString(R.string.detail_user)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter = SPAdapter(this)
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        username = intent.getStringExtra(EXTRA_USER).toString()
        var id = intent.getIntExtra(EXTRA_ID, 0).toInt()
        var avatarUrl = intent.getStringExtra(EXTRA_AVATAR).toString()
        showViewModel()
        viewModel.isLoadingTake.observe(this, this::showLoading)

        var checkTog = false
        CoroutineScope(Dispatchers.IO).launch {
            val sum = viewModel.checkUsr(id)
            withContext(Dispatchers.Main) {
                if (sum != null) {
                    if (sum > 0) {
                        binding.favoritTog.isChecked = true
                        checkTog = true
                    } else {
                        binding.favoritTog.isChecked = false
                        checkTog = false
                    }
                }
            }
        }
        binding.favoritTog.setOnClickListener {
            checkTog = !checkTog
            if (checkTog) {
                viewModel.addToFavorite(id, username, avatarUrl)
            } else {
                viewModel.removeFavor(id)
            }
            binding.favoritTog.isChecked = checkTog
        }
    }

    private fun showViewModel() {
        viewModel.detailUser(username)
        viewModel.listDetailUsers.observe(this) { detailUser ->
            Glide.with(this)
                .load(detailUser.avatarUrl)
                .skipMemoryCache(true)
                .into(binding.imgAvatar)

            binding.tvName.text = detailUser.name
            binding.tvUsername.text = detailUser.login
            binding.tvCompany.text = detailUser.company
            binding.tvLocation.text = detailUser.location
            binding.tvRepositoryValue.text = detailUser.publicRepos
            binding.tvFollowersValue.text = detailUser.followers
            binding.tvFollowingValue.text = detailUser.following
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
