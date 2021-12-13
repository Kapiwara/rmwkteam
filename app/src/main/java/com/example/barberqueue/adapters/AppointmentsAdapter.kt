package com.example.barberqueue.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.barberqueue.R
import com.example.barberqueue.adapter
import com.example.barberqueue.db.OrderForm
import com.example.barberqueue.interfaces.OrderClickView

class AppointmentsAdapter(
    private val appointmentsList: ArrayList<OrderForm>, private val orderClickView: OrderClickView

) :
    RecyclerView.Adapter<AppointmentsAdapter.AppointmentsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.appointments_view_itemview,
            parent, false
        )
        return AppointmentsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AppointmentsViewHolder, position: Int) {
        val currentItem = appointmentsList[position]
        holder.date.text = currentItem.date
        holder.itemView.setBackgroundColor(Color.parseColor("#00ffffff"))
        holder.itemView.setOnClickListener {

            orderClickView.onClickOrder(appointmentsList[position])

        }
    }


    override fun getItemCount(): Int {
        return appointmentsList.size
    }

    class AppointmentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.appointment_date)

    }
}