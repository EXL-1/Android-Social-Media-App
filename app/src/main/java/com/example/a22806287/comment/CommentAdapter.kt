package com.example.a22806287.comment

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a22806287.CommentDataList
import com.example.a22806287.Database
import com.example.a22806287.Global
import com.example.a22806287.R

// Comment Recycler View
class CommentAdapter (
    private val comments: ArrayList<CommentDataList>,
    private val db: Database,
    private val fragmentManager: FragmentManager,
    private val activity: Activity
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    // Defining Values
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtComment : TextView = view.findViewById(R.id.txtComment)
        val txtCommentByUser : TextView = view.findViewById(R.id.txtCommentByUser)
        val txtCommentDate : TextView = view.findViewById(R.id.txtCommentDate)
    }

    // Inflating our Layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_layout, parent, false)
        return ViewHolder(view)
    }

    // Used for each individual Comment Layout
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.txtComment.text = comment.comment

        val student = Global.getStudentByIDDB(activity, comment.studentID)
        holder.txtCommentByUser.text = student[0].fullName
        holder.txtCommentDate.text = "Sent: " + comment.datePosted
    }

    // Get the amount of Comments
    override fun getItemCount() = comments.size

}