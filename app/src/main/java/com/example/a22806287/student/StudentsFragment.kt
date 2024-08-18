package com.example.a22806287.student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a22806287.Database
import com.example.a22806287.Global
import com.example.a22806287.R
import com.example.a22806287.StudentDataList
import com.google.android.material.floatingactionbutton.FloatingActionButton

// Student Fragment
class StudentsFragment : Fragment(R.layout.fragment_students) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Defining Values / Fragments
        val btnAddStudent : FloatingActionButton = view.findViewById(R.id.btnAddStudent)

        val addStudentFragment = AddStudentFragment()

        val db = Database(requireActivity())

        // Get all Students
        fun getStudents(): ArrayList<StudentDataList> {
            val studentsList = ArrayList<StudentDataList>()
            val cursor = db.getAll("student")

            if (cursor.moveToFirst()) {
                do {
                    val studentId = cursor.getInt(cursor.getColumnIndexOrThrow(Database.STUDENT_ID_COL))
                    val studentFullName = cursor.getString(cursor.getColumnIndexOrThrow(Database.STUDENT_FULL_NAME_COL))
                    val studentEmail = cursor.getString(cursor.getColumnIndexOrThrow(Database.STUDENT_SCHOOL_EMAIL_COL))
                    val studentPassword = cursor.getString(cursor.getColumnIndexOrThrow(Database.STUDENT_PASSWORD_COL))
                    val studentDOB = cursor.getString(cursor.getColumnIndexOrThrow(Database.STUDENT_DATE_OF_BIRTH_COL))
                    val dateCreated = cursor.getString(cursor.getColumnIndexOrThrow(Database.DATE_CREATED_COL))

                    val studentInfo = StudentDataList(studentId.toInt(), studentFullName, studentEmail, studentPassword, studentDOB, dateCreated)
                    studentsList.add(studentInfo)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()

            return studentsList
        }

        // Defining the Student Recycler View Adapter
        val rcStudents = view.findViewById<RecyclerView>(R.id.rcStudents);
        rcStudents.layoutManager = LinearLayoutManager(activity);
        val studentAdapter = StudentAdapter(getStudents(), db, parentFragmentManager)
        rcStudents.adapter = studentAdapter

        btnAddStudent.setOnClickListener {
            Global.loadFragment(parentFragmentManager, R.id.flfragment_nav_drawer, addStudentFragment)
        }

    }

}


