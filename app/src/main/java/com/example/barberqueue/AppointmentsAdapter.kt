package com.example.barberqueue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.barberqueue.db.OrderForm
import com.example.barberqueue.db.Service

class AppointmentsAdapter(private val appointmentsList:ArrayList<OrderForm>) :
    RecyclerView.Adapter<AppointmentsAdapter.AppointmentsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.appointments_view_itemview,
            parent, false)
        return AppointmentsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AppointmentsViewHolder, position: Int) {
        val currentItem = appointmentsList[position]
        holder.date.text = currentItem.date
    }

    override fun getItemCount(): Int {
        return appointmentsList.size
    }

    class AppointmentsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val date : TextView = itemView.findViewById(R.id.appointment_date)

    }
}