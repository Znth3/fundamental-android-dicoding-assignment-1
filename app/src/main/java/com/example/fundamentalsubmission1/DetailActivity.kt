package com.example.fundamentalsubmission1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.fundamentalsubmission1.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }


    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"

        val users = intent.getParcelableExtra<User>(EXTRA_USER) as User
        
        with(binding) {
            tvReceivedUsername.text = users.username
            tvReceivedName.text = users.name
            tvReceivedFollowers.text = users.follower.toString()
            tvReceivedFollowing.text = users.following.toString()
            tvReceivedLocation.text = users.location
            tvReceivedRepository.text = users.repository.toString()    
        }

        

        Glide.with(applicationContext)
                .load(users.avatar)
                .apply(RequestOptions().override(55, 55))
                .into(binding.imgReceivedAvatar)
    }
}