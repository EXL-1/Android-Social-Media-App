package com.example.a22806287

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.database.getStringOrNull
import com.example.a22806287.student.AddStudentFragment

// Login Fragment
class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Defining Values / Shared Preferences / Fragments
        val edtEmail : EditText = view.findViewById(R.id.edtEmail)
        val edtPassword : EditText = view.findViewById(R.id.edtPassword)
        val btnLogin : Button = view.findViewById(R.id.btnLogin)
        val btnCreateAccount : Button = view.findViewById(R.id.btnCreateAccount)

        val sp = activity?.getSharedPreferences("Application_info", Context.MODE_PRIVATE)
        val editor = sp?.edit();

        // Click event
        btnLogin.setOnClickListener {

            val strEmail = edtEmail.text.toString()
            val strPassword = edtPassword.text.toString()

                    // Attempts to sign in the credentials of an admin beforehand then the student login.
                    val db = Database(requireActivity())
                    if(db.checkAdmin(strEmail.trim(),strPassword.trim())){

                        Intent(requireActivity(), NavDrawerActivity::class.java).also {
                            startActivity(it)

                        }

                        editor?.putString("userEmail", strEmail)
                        editor?.putString("userPassword", strPassword)
                        editor?.putString("group", "Admin")
                        editor?.putBoolean("isLoggedIn", true)
                        editor?.apply()

                        Toast.makeText(activity,"Admin Logged in Successfully!",Toast.LENGTH_SHORT).show()
                    } else if (db.checkStudent(strEmail.trim().lowercase(),strPassword.trim()) ) {

                        Intent(requireActivity(), NavDrawerActivity::class.java).also {
                            startActivity(it)

                        }

                        val cursor = db.getStudent(strEmail.trim().lowercase())
                        cursor!!.moveToFirst()

                        val studentID =
                            cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.STUDENT_ID_COL))
                        val studentFullName =
                            cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.STUDENT_FULL_NAME_COL))
                        val studentDOB =
                            cursor.getStringOrNull(cursor.getColumnIndexOrThrow(Database.STUDENT_DATE_OF_BIRTH_COL))

                        cursor.close()

                        editor?.putString("userID", studentID)
                        editor?.putString("userName", studentFullName)
                        editor?.putString("userEmail", strEmail)
                        editor?.putString("userPassword", strPassword)
                        editor?.putString("userDOB", studentDOB)
                        editor?.putString("group", "Student")
                        editor?.putBoolean("isLoggedIn", true)
                        editor?.apply()


                        Toast.makeText(activity,"Logged in Successfully!",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity,"Username or password is wrong, please try again",Toast.LENGTH_SHORT).show()
                    }

        }

        btnCreateAccount.setOnClickListener {
            Global.loadFragment(parentFragmentManager, R.id.flFragment, AddStudentFragment())
        }

    }

}