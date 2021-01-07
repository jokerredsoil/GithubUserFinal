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
import com.septian.githubuserfinal.databinding.FragmentFollowingBinding
import com.septian.githubuserfinal.dataclass.User
import com.septian.githubuserfinal.viewmodel.FollowingViewModel


class FollowingFragment : Fragment() {

    companion object {
        private const val ARG_USERNAME = "username"

        fun getUsername(username: String): FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var followingViewModel: FollowingViewModel

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME).toString()

        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.setHasFixedSize(true)
        showLoading(true)

        val listMainAdapter = ListMainAdapter()
        listMainAdapter.notifyDataSetChanged()
        binding.rvFollowing.adapter = listMainAdapter

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        followingViewModel.setFollowing(username)
        followingViewModel.getFollowing().observe(viewLifecycleOwner, { users ->
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
        binding.progressBarFlwng.visibility = View.VISIBLE
    } else {
        binding.progressBarFlwng.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}