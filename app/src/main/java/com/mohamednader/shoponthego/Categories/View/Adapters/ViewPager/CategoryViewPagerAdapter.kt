package com.mohamednader.shoponthego.Categories.View.Adapters.ViewPager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mohamednader.shoponthego.Categories.View.CategoriesFragment
import com.mohamednader.shoponthego.Model.Pojo.CustomCollections.CustomCollection

class CategoryViewPagerAdapter(
    fm: FragmentManager, private val categories: List<CustomCollection>
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(i: Int): Fragment {
        val fragment = CategoriesFragment()
        val args = Bundle().apply {
            putLong("EXTRA_DATA_ID", categories[i].id)
        }
        fragment.arguments = args
        return fragment
    }

    override fun getCount(): Int {
        return categories.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return categories[position].title
    }
}
