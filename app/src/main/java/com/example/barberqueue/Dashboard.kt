package com.example.barberqueue

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.barberqueue.databinding.DashboardBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Dashboard : AppCompatActivity(){
    private var x1 : Float = 0F
    private var y1 : Float = 0F
    private var x2: Float = 0F
    private var y2: Float = 0F

    private lateinit var binding: DashboardBinding
    private lateinit var mViewPager: ViewPager
    private lateinit var mPagerAdapter: PagerAdapter
    private lateinit var tabLayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewPager = findViewById(R.id.viewpager_dashboard)       // te wszystkie pagery to sa do tego zeby w activity dashboard byly fragmenty appointments i new visit

        mPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        mViewPager.adapter = mPagerAdapter
        mViewPager.offscreenPageLimit = 2


        mViewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{


            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                changingTabs(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        mViewPager.currentItem = 0
        tabLayout = findViewById(R.id.navigation_menu)
        tabLayout.setupWithViewPager(mViewPager)
        //val accMngBtn = findViewById<Button>(R.id.acc_mng_btn)
        //val logoutBtn = findViewById<Button>(R.id.logout_btn)

        binding.accMngBtn.setOnClickListener{ openActivityAccountManagement() }
        binding.logoutBtn.setOnClickListener{
            Firebase.auth.signOut()
            finish()
        }



    }

    private fun changingTabs(position: Int) {

        if(position == 0){

        }
        if(position == 1){

        }
    }

    //funkcja do poruszania sie po ui w poziomie
    override fun onTouchEvent(touchEvent : MotionEvent): Boolean {

        when(touchEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = touchEvent.x
                y1 = touchEvent.y

            }

            MotionEvent.ACTION_UP -> {
                x2 = touchEvent.x
                y2 = touchEvent.y
                if(x1 < x2 && y1 <= y2+100 && y1 >= y2-100){
                    openActivityMenu()
                    Log.e("position", "$x1,$y1     $x2,$y2")
                }
                else if(x1 > x2 && y1 <= y2+100 && y1 >= y2-100){
                    openActivitySTH()
                    Log.e("position", "$x1,$y1     $x2,$y2")
                }
                else {
                    Log.e("position", "nothing happens hehe")
                }

            }
        }


        return false
    }


    private fun openActivityAccountManagement() {
        val intent = Intent(this, AccountManagement::class.java)
        startActivity(intent)
    }

    private fun openActivityMenu() {
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
    }

    private fun openActivitySTH() {
        val intent = Intent(this, Right::class.java)
        startActivity(intent)
    }





}
