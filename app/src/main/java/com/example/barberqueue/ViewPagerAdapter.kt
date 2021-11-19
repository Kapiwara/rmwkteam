package com.example.barberqueue

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

internal class ViewPagerAdapter (fm:FragmentManager?):
        FragmentPagerAdapter(fm!!){
            override fun getItem(position: Int): Fragment {


                return when (position){
                    0 -> {
                        appointmentsFragment()
                    }

                    1 -> {
                        newVisitFragment()
                    }
                    else -> appointmentsFragment()
                }
            }
    override  fun  getCount(): Int { return 2}
    override  fun getPageTitle(position: Int): CharSequence{

        if(position == 0) return "Appointments"

        else if(position == 1) return "New visit"

        else return "error"
    }
        }