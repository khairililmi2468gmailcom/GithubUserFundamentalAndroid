package com.example.appgithubusernaviapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appgithubusernaviapi.database.FavUsers
import com.example.appgithubusernaviapi.databinding.ActivityFavoriteBinding
import com.example.appgithubusernaviapi.databinding.ActivityItemFavoriteBinding
import com.example.appgithubusernaviapi.model.UserData2

class UserAdapFavor :RecyclerView.Adapter<UserAdapFavor.ListViewHolderFavorite>(){
    private val listUserFavorite = ArrayList<UserData2>()
    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    fun setData(data: ArrayList<UserData2>) {
        val diffCallback = DiffUtilCallbackFavorite(listUserFavorite, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listUserFavorite.clear()
        listUserFavorite.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }


    class ListViewHolderFavorite(private val binding: ActivityItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) { // Perubahan di sini
        fun bind(user: UserData2) {
            binding.tvUsername.text = user.username
            binding.tvAccount.text = user.id.toString()
            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .into(binding.imgAvatar)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserData2)
    }
    class DiffUtilCallbackFavorite(private val oldList: List<UserData2>, private val newList: List<UserData2>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.javaClass == newItem.javaClass
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.hashCode() == newItem.hashCode()
        }

        @Override
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolderFavorite  {
        val binding = ActivityItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolderFavorite(binding)
    }

    override fun getItemCount(): Int {
        return listUserFavorite.size
    }

    override fun onBindViewHolder(holder: ListViewHolderFavorite, position: Int) {
        val user = listUserFavorite[position]
        holder.bind(user)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUserFavorite[position])
        }
    }


}