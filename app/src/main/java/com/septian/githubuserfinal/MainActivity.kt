package com.septian.githubuserfinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.septian.githubuserfinal.adapters.ListMainAdapter
import com.septian.githubuserfinal.databinding.ActivityMainBinding
import com.septian.githubuserfinal.dataclass.User
import com.septian.githubuserfinal.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainAdapter: ListMainAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        mainAdapter = ListMainAdapter()
        mainAdapter.notifyDataSetChanged()
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        supportActionBar?.title = "Github User Search"
        searchUserList()
    }

    private fun searchUserList() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    return true
                } else {
                    showLoading(true)
                    mainViewModel.setSearchUser(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        mainViewModel.getSearchUser().observe(this, { data ->
            Log.e(TAG, "ObserveData: $data")
            if (data != null) {
                binding.rvUser.visibility = View.VISIBLE
                mainAdapter.setData(data)
                binding.rvUser.layoutManager = LinearLayoutManager(this)
                binding.rvUser.setHasFixedSize(true)
                binding.rvUser.adapter = mainAdapter
                showLoading(false)

                mainAdapter.setOnItemClickCallback(object : ListMainAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: User) {
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.EXTRA_USER, data)
                        startActivity(intent)
                        showLoading(false)
                        showSelectedUser(data)
                    }
                })
            }
        })
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
                startActivity(i)
                true
            }
            else -> true
        }
    }

//miscellaneous

    private fun showSelectedUser(user: User) {
        Toast.makeText(this, "Kamu memilih ${user.login}", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) = if (state) {
        binding.progressBar.visibility = View.VISIBLE
    } else {
        binding.progressBar.visibility = View.GONE
    }
}