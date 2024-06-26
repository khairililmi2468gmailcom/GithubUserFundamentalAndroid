package com.example.appgithubusernaviapi.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appgithubusernaviapi.ui.MainFollowers
import com.example.appgithubusernaviapi.ui.MainFollowing

class SPAdapter (activity: AppCompatActivity) : FragmentStateAdapter(activity){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MainFollowers()
            1 -> fragment = MainFollowing()
        }

        return fragment as Fragment
    }
}