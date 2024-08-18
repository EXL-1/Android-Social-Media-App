package com.example.a22806287

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.widget.TextView
import android.widget.Toast
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.text.SimpleDateFormat
import java.util.Calendar

// Student Data Class
data class StudentDataList(val studentID: Int, val fullName: String, val email: String, val password: String, val dob: String, val dateCreated: String)

// Post Data Class
data class PostDataList(val postID: Int,val studentID: Int, val postTitle: String, val postDescription: String, val datePosted: String)

// Comment Data Class
data class CommentDataList(val commentID: Int, val postID: Int, val studentID: Int, val comment: String, val datePosted: String)

// Global Class contains various functions that can be used everywhere
class Global {
    companion object {

        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val currentTime: String = formatter.format(calendar.time)

        // isValueEmpty Function that checks for a single empty value
        @SuppressLint("SetTextI18n")
        fun isValueEmpty(activity : Activity, valueOne : String): Int {

            var check = 0
            check = if(valueOne.isEmpty()){


                Toast.makeText(activity,"Field is empty!", Toast.LENGTH_LONG).show()
                1
            } else {

                0
            }

            return check
        }

        // isEmpty Function that checks for empty values
        @SuppressLint("SetTextI18n")
        fun isEmpty(activity : Activity, valueOne : String, valueTwo : String): Int {

            var check = 0
            check = if(valueOne.isEmpty() || valueTwo.isEmpty()){


                Toast.makeText(activity,"One or More fields are empty!", Toast.LENGTH_LONG).show()
                1
            } else {

                0
            }

            return check
        }

        // Add Student Information to the Database
        fun addStudentDB(activity : Activity, s : ArrayList<StudentDataList>, currentTime : String) {
            val db = Database(activity)
            db.addStudent(s[0].fullName,s[0].email.lowercase(), s[0].password, s[0].dob, currentTime)
            db.close()
        }

        // Update Student Information to the Database
        fun updateStudentDB(activity : Activity, s : ArrayList<StudentDataList>, currentTime : String) {
            val db = Database(activity)
            db.updateStudent(s[0].studentID, s[0].fullName,s[0].email.lowercase(), s[0].password, s[0].dob, currentTime)
            db.close()
        }

        // Add Post Information to the Database
        fun addPostDB(activity : Activity, p : ArrayList<PostDataList>, currentTime : String) {
            val db = Database(activity)
            db.addPost(p[0].studentID, p[0].postTitle, p[0].postDescription, currentTime)
            db.close()
        }

        // Update Post Information to the Database
        fun updatePostDB(activity : Activity, p : ArrayList<PostDataList>, currentTime : String) {
            val db = Database(activity)
            db.updatePost(p[0].postID, p[0].postTitle, p[0].postDescription, currentTime)
            db.close()
        }

        // Add Comment Information to the Database
        fun addCommentDB(activity : Activity, c : ArrayList<CommentDataList>) {
            val db = Database(activity)
            db.addComment(c[0].studentID, c[0].postID, c[0].comment, c[0].datePosted)
            db.close()
        }

        // Get student Information from the Database
        fun getStudentDB(activity: Activity, email: String): ArrayList<StudentDataList> {

            val studentArray = ArrayList<StudentDataList>()

            val db = Database(activity)
            val cursor = db.getStudent(email.trim().lowercase())
            cursor!!.moveToFirst()

            val studentID =
                cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.STUDENT_ID_COL))
            val studentFullName =
                cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.STUDENT_FULL_NAME_COL))
            val studentEmail =
                cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.STUDENT_SCHOOL_EMAIL_COL))
            val studentPassword =
                cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.STUDENT_PASSWORD_COL))
            val studentDOB =
                cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.STUDENT_DATE_OF_BIRTH_COL))
            val dateCreated =
                cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.DATE_CREATED_COL))

            cursor.close()
            db.close()

            val studentInfo =
                StudentDataList(
                    studentID!!.toInt(), studentFullName.toString(),
                    studentEmail.toString(), studentPassword.toString(), studentDOB.toString(), dateCreated.toString())


            studentArray.add(studentInfo)

            return studentArray
        }

        // Get student Information from the Database
        fun getStudentByIDDB(activity: Activity, studentID: Int): ArrayList<StudentDataList> {

            val studentArray = ArrayList<StudentDataList>()

            val db = Database(activity)
            val cursor = db.getStudentByID(studentID)
            cursor!!.moveToFirst()

            val studentFullName =
                cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.STUDENT_FULL_NAME_COL))
            val studentEmail =
                cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.STUDENT_SCHOOL_EMAIL_COL))
            val studentPassword =
                cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.STUDENT_PASSWORD_COL))
            val studentDOB =
                cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.STUDENT_DATE_OF_BIRTH_COL))
            val dateCreated =
                cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.DATE_CREATED_COL))

            cursor.close()
            db.close()

            val studentInfo =
                StudentDataList(
                    studentID, studentFullName.toString(),
                    studentEmail.toString(), studentPassword.toString(), studentDOB.toString(), dateCreated.toString())


            studentArray.add(studentInfo)

            return studentArray
        }

        // Get post Information from the Database
        fun getPostDB(activity: Activity, postID: Int): ArrayList<PostDataList> {

            val postArray = ArrayList<PostDataList>()

            val db = Database(activity)
            val cursor = db.getPost(postID)
            cursor!!.moveToFirst()

            val studentID =
                cursor.getIntOrNull(cursor.getColumnIndexOrThrow(Database.STUDENT_ID_COL))
            val postTitle =
                cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.POST_TITLE_COL))
            val postDescription =
                cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.POST_DESCRIPTION_COL))
            val datePosted=
                cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.DATE_CREATED_COL))

            cursor.close()
            db.close()

            val postInfo =
                PostDataList(
                    postID, studentID!!,
                    postTitle.toString(), postDescription.toString(), datePosted.toString())


            postArray.add(postInfo)

            return postArray
        }

        // Initiates the Date Picker
        fun showDatePicker(activity: Activity, value : TextView) {
            val datePickerDialog = DatePickerDialog(
                activity,
                R.style.Theme_DatePicker, { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->

                    calendar.set(year, monthOfYear, dayOfMonth)
                    val formattedDate = formatter.format(calendar.time)
                    value.text = formattedDate
                },

                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Function that loads fragments.
        fun loadFragment(fragmentManager: FragmentManager, fragmentLayout : Int, fragment: Fragment,) {
            fragmentManager.beginTransaction().apply {
                replace(fragmentLayout, fragment)
                addToBackStack(null)
                commit()
            }
        }

    }
}