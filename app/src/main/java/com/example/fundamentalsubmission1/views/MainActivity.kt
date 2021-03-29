package com.example.fundamentalsubmission1.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundamentalsubmission1.adapters.UserAdapter
import com.example.fundamentalsubmission1.api.ApiHelper
import com.example.fundamentalsubmission1.api.RetrofitBuilder
import com.example.fundamentalsubmission1.databinding.ActivityMainBinding
import com.example.fundamentalsubmission1.models.User
import com.example.fundamentalsubmission1.utils.Status
import com.example.fundamentalsubmission1.viewmodels.UserViewModel
import com.example.fundamentalsubmission1.viewmodels.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUsers: RecyclerView
    private lateinit var viewModel: UserViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initUi()
        initObserver()

    }

    private fun initUi(){
        supportActionBar?.title = "Github User's"

        rvUsers = binding.rvUsers
        rvUsers.setHasFixedSize(true)

        showRecyclerList()
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService)))
            .get(UserViewModel::class.java)
    }

    private fun showRecyclerList() {
        rvUsers.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter(arrayListOf())
        rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClick(data: User) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, data.login)
                startActivity(intent)
            }
        })
    }

    private fun initObserver(){
        viewModel.apiCallWithRepository().observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.pbMain.visibility = View.GONE
                        resource.data?.let { users -> getList(users) }
                    }

                    Status.LOADING -> {
                        binding.pbMain.visibility = View.VISIBLE
                    }

                    Status.ERRORS -> {
                        binding.pbMain.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun getList(users: ArrayList<User>) {
        adapter.apply {
            addUsers(users)
            notifyDataSetChanged()
        }
    }
}