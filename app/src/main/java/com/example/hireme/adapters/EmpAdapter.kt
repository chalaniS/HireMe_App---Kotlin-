package com.example.hireme.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.hireme.R
import com.example.hireme.models.FeedbackModel

class EmpAdapter(private val rvList : ArrayList<FeedbackModel>):
        RecyclerView.Adapter<EmpAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.review_list_item,parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val currentEmp = rvList[position]
        holder.tvReview.text = currentEmp.tvContent
    }

    override fun getItemCount(): Int {
        return rvList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvReview: TextView = itemView.findViewById(R.id.tvCont)
    }

}
