package com.example.barberqueue

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.databinding.DashboardBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Dashboard : AppCompatActivity(){
    private var x1 : Float = 0F
    private var y1 : Float = 0F
    private var x2: Float = 0F
    private var y2: Float = 0F

    private lateinit var binding: DashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val addNewVisitBtn = findViewById< Button >(R.id.add_new_visit_btn,)
        binding.addNewVisitBtn.setOnClickListener{ openActivityNewVisit()}

        //val accMngBtn = findViewById<Button>(R.id.acc_mng_btn)
        //val logoutBtn = findViewById<Button>(R.id.logout_btn)

        binding.accMngBtn.setOnClickListener{ openActivityAccountManagement() }
        binding.logoutBtn.setOnClickListener{
            Firebase.auth.signOut()
            finish()
        }
        binding.logo.setOnClickListener{
            openActivityContact();
        }



    }

    private fun openActivityContact() {
        val intent = Intent(this, ContactData::class.java)
        startActivity(intent)
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

    private fun openActivityNewVisit() {
        val intent = Intent(this,NewVisit::class.java)
        startActivity(intent)
    }





}
