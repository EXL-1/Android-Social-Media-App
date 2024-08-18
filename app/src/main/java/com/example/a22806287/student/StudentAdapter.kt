package com.example.a22806287.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a22806287.Database
import com.example.a22806287.Global
import com.example.a22806287.R
import com.example.a22806287.StudentDataList

// Student Recycler View
class StudentAdapter(
    private val students: ArrayList<StudentDataList>,
    private val db: Database,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    // Defining Values
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtStudentEmail : TextView = view.findViewById(R.id.txtStudentName)
        val btnDeleteStudent : ImageButton = view.findViewById(R.id.btnStudentDelete)
    }

    // Inflating the Layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_layout, parent, false)
        return ViewHolder(view)
    }

    // Used for each individual Student Layout
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        holder.txtStudentEmail.text = student.fullName

        // Delete Student Button
        holder.btnDeleteStudent.setOnClickListener {
            db.delete("comment", Database.STUDENT_ID_COL, student.studentID)
            db.delete("post", Database.STUDENT_ID_COL, student.studentID)
            db.delete("student", Database.STUDENT_ID_COL, student.studentID)
            students.removeAt(position)
            notifyItemRemoved(position)

        }

        // Edit Student Button
        holder.txtStudentEmail.setOnClickListener {

            val bundle = Bundle()
            val editStudentFragment = EditStudentFragment()
            bundle.putString("STUDENT_ID", student.studentID.toString())
            bundle.putString("STUDENT_EMAIL", student.email)
            editStudentFragment.arguments = bundle

            Global.loadFragment(fragmentManager, R.id.flfragment_nav_drawer, editStudentFragment)
        }
    }

    // Get the amount of Students
    override fun getItemCount() = students.size

}