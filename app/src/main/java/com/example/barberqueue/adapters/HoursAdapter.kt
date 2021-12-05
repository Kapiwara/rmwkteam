package com.example.barberqueue.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.barberqueue.HoursViewModel
import com.example.barberqueue.R
import com.example.barberqueue.interfaces.FromMakeAppointmentToSummary

class HoursAdapter(private val mList: List<HoursViewModel>, var action: FromMakeAppointmentToSummary) : RecyclerView.Adapter<HoursAdapter.ViewHolder>() {

    private var focusedPos: Int = -1

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_hours, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val hoursViewModel = mList[position]


        // sets the text to the textview from our itemHolder class
        holder.textView.text = hoursViewModel.text

        if(focusedPos == -1){
            holder.itemView.setBackgroundResource(R.drawable.button_design_2)
        }
        else{
            if (focusedPos == position){
                holder.itemView.setBackgroundResource(R.drawable.button_design_1)
            }
            else{
                holder.itemView.setBackgroundResource(R.drawable.button_design_2)
            }
        }

        holder.itemView.setOnClickListener {
            if(focusedPos != position) {
                notifyDataSetChanged()
                focusedPos = position
                action.getSelectedTime(hoursViewModel.text)
            }
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
    }
}
