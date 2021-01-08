package com.septian.githubuserfinal

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.septian.githubuserfinal.adapters.FavoriteAdapter
import com.septian.githubuserfinal.databinding.ActivityFavoriteBinding
import com.septian.githubuserfinal.dataclass.User
import com.septian.githubuserfinal.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.septian.githubuserfinal.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var mainAdapter: FavoriteAdapter
    private lateinit var binding: ActivityFavoriteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite User's "
        showLoading(true)

        mainAdapter = FavoriteAdapter(this)

        GlobalScope.launch(Dispatchers.Main) {
            val deferredUser = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val fav = deferredUser.await()
            if (fav.size > 0) {
                mainAdapter.listFav = fav

            } else {
                mainAdapter.listFav = ArrayList()

            }
        }

        showRecyclerView()
    }

    private fun showRecyclerView() {
        with(binding) {
            rvFavorite.adapter = mainAdapter
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavorite.setHasFixedSize(true)
            showLoading(false)
        }
    }


    //    miscellaneous
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.favBtnMain) {
            val i = Intent(this, FavoriteActivity::class.java)
            startActivity(i)
            true
        } else true
    }

//miscellaneous

    private fun showSelectedUser(user: User) {
        Toast.makeText(this, "Kamu memilih ${user.login}", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) = if (state) {
        binding.progressFavorite.visibility = View.VISIBLE
    } else {
        binding.progressFavorite.visibility = View.GONE
    }

}