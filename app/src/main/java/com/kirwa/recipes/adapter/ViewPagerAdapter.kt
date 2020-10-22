package com.kirwa.recipes.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(manager: FragmentManager): FragmentPagerAdapter(manager) {
    private val mFragmentList=ArrayList<Fragment>()
    private val mFragmentTitleList=ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment, Title:String){
        mFragmentList.add(fragment)
        mFragmentTitleList.add(Title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

}