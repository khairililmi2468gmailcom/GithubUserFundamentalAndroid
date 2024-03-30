package com.example.appgithubusernaviapi.ui.favor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appgithubusernaviapi.adapter.UserAdapFavor
import com.example.appgithubusernaviapi.database.FavUsers
import com.example.appgithubusernaviapi.databinding.ActivityFavoriteBinding
import com.example.appgithubusernaviapi.model.UserData2
import com.example.appgithubusernaviapi.ui.DetailUser
import com.example.appgithubusernaviapi.viewModel.FavorModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFavoriteBinding
    private lateinit var adapter: UserAdapFavor
    private lateinit var viewModel : FavorModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapFavor()
        adapter.notifyDataSetChanged()
        viewModel = ViewModelProvider(this).get(FavorModel::class.java)
        adapter.setOnItemClickCallback(object: UserAdapFavor.OnItemClickCallback{
            override fun onItemClicked(data: UserData2) {
                Intent(this@FavoriteActivity, DetailUser::class.java).also {
                    it.putExtra(DetailUser.EXTRA_USER, data.username)
                    it.putExtra(DetailUser.EXTRA_ID, data.id)
                    it.putExtra(DetailUser.EXTRA_AVATAR, data.avatarUrl)
                    startActivity(it)
                }
            }
        })

        binding.apply {
            idUsr.setHasFixedSize(true)
            idUsr.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            idUsr.adapter = adapter
        }
        observeFavoriteData()

    }
    private fun observeFavoriteData() {
        viewModel.getFavorUsers()?.observe(this, { favUsersList ->
            if (favUsersList != null && favUsersList.isNotEmpty()) {
                val userDataList = mapList(favUsersList)
                adapter.setData(userDataList)
                binding.textFavoriteEmpty.visibility = View.GONE
                binding.idUsr.visibility = View.VISIBLE
            } else {
                binding.textFavoriteEmpty.visibility = View.VISIBLE
                binding.idUsr.visibility = View.GONE
            }
        })
    }
    private fun mapList(usr:List<FavUsers>): ArrayList<UserData2>{
        val listUsr = ArrayList<UserData2>()
        for(user in usr){
            val userMap = UserData2(
                user.id,
                user.login,
                user.avatarUrl

            )
            listUsr.add(userMap)
        }
        return listUsr
    }
}