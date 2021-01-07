package com.septian.githubuserfinal

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.septian.githubuserfinal.adapters.ViewPagerAdapter
import com.septian.githubuserfinal.db.DatabaseContract
import com.septian.githubuserfinal.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.septian.githubuserfinal.databinding.ActivityDetailBinding
import com.septian.githubuserfinal.dataclass.User
import com.septian.githubuserfinal.viewmodel.DetailViewModel
import com.septian.githubuserfinal.db.UserHelper

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"
        private val TAG = DetailActivity::class.java.simpleName
    }

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding
    private lateinit var userHelper: UserHelper
    private lateinit var uriWithId: Uri

    private var dataIntent: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val viewPagerAdapter = ViewPagerAdapter(this, supportFragmentManager)
        viewPagerAdapter.username = dataIntent?.login
        binding.viewPager.adapter = viewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        dataIntent = intent.getParcelableExtra(EXTRA_USER)
        val id = dataIntent?.id
        val username = dataIntent?.id
        val html = dataIntent?.html
        val avatar = dataIntent?.avatar

        dataIntent?.let { setData -> setDetailData(setData) }


        supportActionBar?.title = "${dataIntent?.login} detail"
        showLoading(true)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        var statusFavorite = false
        binding.btnFav.setOnClickListener{
            if (!statusFavorite){
                val values = contentValuesOf(
                    DatabaseContract.UserColumns._ID to id,
                    DatabaseContract.UserColumns.USERNAME to username,
                    DatabaseContract.UserColumns.HTML to html,
                    DatabaseContract.UserColumns.AVATAR to avatar

                )
                contentResolver.insert(CONTENT_URI,values)
                statusFavorite = !statusFavorite
                setStatusFavorite(statusFavorite)

            }else{
                uriWithId = Uri.parse("$CONTENT_URI/$id")
                contentResolver.delete(uriWithId,null,null)
                statusFavorite = !statusFavorite
                setStatusFavorite(statusFavorite)
            }
        }
        val cursor: Cursor = userHelper.queryById(id.toString())
        if (cursor.moveToNext()){
            statusFavorite = !statusFavorite
            setStatusFavorite(statusFavorite)
        }

    }

    private fun setDetailData(data: User) {
        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)
        detailViewModel.setDetailUser(data.login)
        detailViewModel.getDetailUser().observe(this, { user ->
            if (user != null) {
                with(binding) {
                    txtUsername.text = user.login
                    txtName.text = user.name
                    txtHtml.text = user.html
                    txtLocation.text = user.location
                    txtCompany.text = user.company
                    txtRepository.text = user.repository.toString()
                    txtFollowers.text = user.followers.toString()
                    txtFollowing.text = user.following.toString()

                    Glide.with(this@DetailActivity)
                        .load(user.avatar)
                        .apply(RequestOptions().override(55, 55))
                        .error(R.drawable.ic_launcher_background)
                        .into(imgAvatar)
                    Log.e(TAG, "Observer viewModel: $user")
                }
                showLoading(false)
                showSelectedUser(user)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        Toast.makeText(this, "Kamu memilih ${user.login}", Toast.LENGTH_SHORT).show()
    }


    private fun showLoading(state: Boolean) = if (state) {
        binding.progressDetail.visibility = View.VISIBLE
    } else {
        binding.progressDetail.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.homeBtn -> {
                val i = Intent(this, MainActivity::class.java)
                RequestOptions.fitCenterTransform()
                startActivity(i)
                true
            }
            else -> true
        }
    }


    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_24)
            Toast.makeText(this, "Telah Dihapus dari Favorite", Toast.LENGTH_SHORT)
                .show()

        } else {
            binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            Toast.makeText(this, "Telah Ditambahkan ke Favorite", Toast.LENGTH_SHORT).show()
        }
    }
}