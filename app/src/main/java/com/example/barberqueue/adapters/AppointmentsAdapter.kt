package com.example.barberqueue.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.barberqueue.Dashboard
import com.example.barberqueue.R
import com.example.barberqueue.db.OrderForm

class AppointmentsAdapter(
    private val appointmentsList: ArrayList<OrderForm>, private val orderClickView: Dashboard

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
        holder.hour.text = currentItem.hour
        if (currentItem.isCanceled == true){
            holder.status.text = "Canceled"
        }else if (currentItem.isCanceled == false && currentItem.isAccepted == true){
            holder.status.text = "Confirmed"
        }else{
            holder.status.text = "Scheduled"
        }
        holder.itemView.setBackgroundColor(Color.parseColor("#00ffffff"))
        holder.itemView.setOnClickListener {

            orderClickView.onClickOrder(appointmentsList[position], holder.bindingAdapterPosition)

        }
    }


    override fun getItemCount(): Int {
        return appointmentsList.size
    }

    class AppointmentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.appointment_date)
        val hour: TextView = itemView.findViewById(R.id.appointment_hour)
        val status: TextView = itemView.findViewById(R.id.appointment_isAccepted)

    }
}