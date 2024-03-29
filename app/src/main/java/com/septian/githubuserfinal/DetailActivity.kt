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
import com.septian.githubuserfinal.databinding.ActivityDetailBinding
import com.septian.githubuserfinal.dataclass.User
import com.septian.githubuserfinal.db.DatabaseContract
import com.septian.githubuserfinal.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.septian.githubuserfinal.db.UserHelper
import com.septian.githubuserfinal.viewmodel.DetailViewModel

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

        dataIntent = intent.getParcelableExtra(EXTRA_USER)
        val id = dataIntent?.id
        val username = dataIntent?.login.toString()
        val html = dataIntent?.html.toString()
        val avatar = dataIntent?.avatar.toString()

        val viewPagerAdapter = ViewPagerAdapter(this, supportFragmentManager)
        viewPagerAdapter.username = username
        binding.viewPager.adapter = viewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)



        dataIntent?.let { setData -> setDetailData(setData) }


        supportActionBar?.title = "$username detail"
        showLoading(true)
//set
        userHelper = UserHelper(applicationContext)
        userHelper.open()

        var statusFavorite = false
        setStatusFavorite(statusFavorite)
        binding.btnFav.setOnClickListener {
            if (!statusFavorite) {
                val values = contentValuesOf(
                    DatabaseContract.UserColumns._ID to id,
                    DatabaseContract.UserColumns.USERNAME to username,
                    DatabaseContract.UserColumns.HTML to html,
                    DatabaseContract.UserColumns.AVATAR to avatar

                )
                contentResolver.insert(CONTENT_URI, values)
                statusFavorite = !statusFavorite
                setStatusFavorite(statusFavorite)
                Toast.makeText(
                    this,
                    "$username Telah Ditambahkan ke Favorite",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                uriWithId = Uri.parse("$CONTENT_URI/$id")
                contentResolver.delete(uriWithId, null, null)
                statusFavorite = !statusFavorite
                setStatusFavorite(statusFavorite)
                Toast.makeText(
                    this,
                    "$username Telah Dihapus dari Favorite",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val cursor: Cursor = userHelper.queryById(id.toString())
        if (cursor.moveToNext()) {
            statusFavorite = true
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.homeBtn -> {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                return true
            }
            R.id.favBtn -> {
                val i = Intent(this, FavoriteActivity::class.java)
                startActivity(i)
                return true
            }
            R.id.settingBtn -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }

        }
        return true
    }


//    miscellaneous

    private fun showSelectedUser(user: User) {
        Toast.makeText(this, "Kamu memilih ${user.login}", Toast.LENGTH_SHORT).show()
    }


    private fun showLoading(state: Boolean) = if (state) {
        binding.progressDetail.visibility = View.VISIBLE
    } else {
        binding.progressDetail.visibility = View.GONE
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_24)

        } else {
            binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)

        }
    }
}