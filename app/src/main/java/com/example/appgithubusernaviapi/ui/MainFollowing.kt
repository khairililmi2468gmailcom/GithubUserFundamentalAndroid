package com.example.appgithubusernaviapi.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appgithubusernaviapi.adapter.FollowesAdapter
import com.example.appgithubusernaviapi.databinding.FragmentMainFollowingBinding
import com.example.appgithubusernaviapi.model.ResponseFollowes
import com.example.appgithubusernaviapi.viewModel.FollowingModel


class MainFollowing : Fragment() {
    private val viewModel: FollowingModel by viewModels()
    private val adapter = FollowesAdapter()

    private lateinit var binding: FragmentMainFollowingBinding
    private val _binding get() = binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainFollowingBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showViewModel()
        showRecyclerView()
        viewModel.isLoadingTake.observe(viewLifecycleOwner, this::showLoading)
    }

    private fun showViewModel() {
        viewModel.following(DetailUser.username)
        viewModel.listFollowing.observe(viewLifecycleOwner) { following ->
            if (following.size != 0) {
                adapter.setData(following)
            } else {
                Toast.makeText(context, "Following Not Found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showRecyclerView() {
        binding.rvFollowing.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollowing.setHasFixedSize(true)
        binding.rvFollowing.adapter = adapter

        adapter.setOnItemClickCallback { data -> selectedUser(data) }
    }

    private fun selectedUser(user: ResponseFollowes) {
        Toast.makeText(context, "You choose ${user.login}", Toast.LENGTH_SHORT).show()

        val i = Intent(activity, DetailUser::class.java)
        i.putExtra(DetailUser.EXTRA_USER, user.login)
        startActivity(i)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.following(DetailUser.username)
    }
}