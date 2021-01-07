package com.septian.githubuserfinal.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.septian.githubuserfinal.DetailActivity
import com.septian.githubuserfinal.adapters.ListMainAdapter
import com.septian.githubuserfinal.databinding.FragmentFollowerBinding
import com.septian.githubuserfinal.dataclass.User
import com.septian.githubuserfinal.viewmodel.FollowerViewModel


class FollowerFragment : Fragment() {

    companion object {
        private const val ARG_USERNAME = "username"

        fun getUsername(username: String): FollowerFragment {
            val fragment = FollowerFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var followerViewModel: FollowerViewModel

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME).toString()

        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowers.setHasFixedSize(true)
        showLoading(true)

        val listMainAdapter = ListMainAdapter()
        listMainAdapter.notifyDataSetChanged()
        binding.rvFollowers.adapter = listMainAdapter

        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowerViewModel::class.java)
        followerViewModel.setFollower(username)
        followerViewModel.getFollowers().observe(viewLifecycleOwner, { users ->
            if ((users != null) && (users.size != 0)) {
                listMainAdapter.setData(users)
            }
            showLoading(false)
        })

        listMainAdapter.setOnItemClickCallback(object : ListMainAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, data)
                startActivity(intent)

            }
        })
    }

    private fun showLoading(state: Boolean) = if (state) {
        binding.progressBarFlwr.visibility = View.VISIBLE
    } else {
        binding.progressBarFlwr.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}