package com.example.gachon_club.Club.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gachon_club.Club.Adapter.BoardRecyclerAdapter.ViewHolder
import com.example.gachon_club.Club.Model.Board
import com.example.gachon_club.Club.NoticeFragment
import com.example.gachon_club.R


class BoardRecyclerAdapter(var boardList: ArrayList<Board>, var context: Context, var itemClick: (Board) -> Unit)
    : RecyclerView.Adapter<ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.list_notice_view,parent, false), itemClick)
    }

    override fun getItemCount(): Int {
        return boardList.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(boardList[position],context)

    }

    inner class ViewHolder (itemView: View?, itemClick: (Board) -> Unit) : RecyclerView.ViewHolder(itemView!!){

        val title = itemView?.findViewById<TextView>(R.id.text_BoardTitle)
        val info = itemView?.findViewById<TextView>(R.id.text_Boardinfo)
        val content = itemView?.findViewById<TextView>(R.id.text_Boardcontent)

        fun bind(itemBoard: Board?, context: Context){
            title?.text = itemBoard?.title
            info?.text = itemBoard?.name
            content?.text = itemBoard?.content
            itemView.setOnClickListener { itemClick(itemBoard!!) }
        }

    }
}