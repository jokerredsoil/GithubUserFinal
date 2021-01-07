package com.septian.githubuserfinal.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.septian.githubuserfinal.R
import com.septian.githubuserfinal.fragment.FollowerFragment
import com.septian.githubuserfinal.fragment.FollowingFragment

class ViewPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var username: String? = null

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowerFragment.getUsername(username.toString())
            1 -> fragment = FollowingFragment.getUsername(username.toString())

        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> context.resources.getString(R.string.followers)
            else -> context.resources.getString(R.string.following)
        }

    }

}