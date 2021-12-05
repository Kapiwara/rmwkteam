package com.example.barberqueue.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.barberqueue.R
import com.example.barberqueue.SummaryViewModel

class SummaryAdapter(private val summaryList: List<SummaryViewModel>) : RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.summary_service_item,
        parent, false)
        return SummaryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder,@SuppressLint("RecyclerView") position: Int) {
        val summaryViewModel = summaryList[position]

        holder.textView.text = summaryViewModel.text
    }

    override fun getItemCount() = summaryList.size


    class SummaryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.findViewById(R.id.textview_summary)

    }
}