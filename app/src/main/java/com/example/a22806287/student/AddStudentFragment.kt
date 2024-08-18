package com.example.a22806287.student

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.a22806287.Database
import com.example.a22806287.Global
import com.example.a22806287.LoginFragment
import com.example.a22806287.R
import com.example.a22806287.StudentDataList
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

// Add Student Fragment
class AddStudentFragment : Fragment(R.layout.fragment_add_student) {

    private val studentArray = ArrayList<StudentDataList>()

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Defining Values / Shared Preferences / Fragments
        val sp = activity?.getSharedPreferences("Application_info", Context.MODE_PRIVATE)
        val group = sp?.getString("group", "")

        val fullNameContainer : TextInputLayout = view.findViewById(R.id.fullNameContainer)
        val emailContainer : TextInputLayout = view.findViewById(R.id.emailContainer)
        val passwordContainer : TextInputLayout = view.findViewById(R.id.passwordContainer)

        val edtFullName : TextInputEditText = view.findViewById(R.id.edtFullName)
        val edtEmail : TextInputEditText = view.findViewById(R.id.edtNewEmail)
        val edtPassword : TextInputEditText = view.findViewById(R.id.edtNewPassword)

        val txtCreateStudent = view.findViewById<TextView>(R.id.txtCreateStudent)
        val txtDate : TextView = view.findViewById(R.id.txtDate)
        val txtDateError : TextView = view.findViewById(R.id.txtDateError)
        val btnPickDate : Button = view.findViewById(R.id.btnPickDate)
        val btnCreateStudent : Button = view.findViewById(R.id.btnCreateStudent)
        val btnCancelCreateStudent : Button = view.findViewById(R.id.btnCancelCreateStudent)

        val loginFragment = LoginFragment()
        val studentsFragment = StudentsFragment()

        // Detects whether the Full Name is valid
        fun validFullName(): String? {
            val fullName = edtFullName.text.toString()
            if (fullName.length < 5)
            {
                return "Full name must be more than 5 characters!"
            }

            return null
        }

        // Detects whether the Email is Valid
        fun validEmail(): String? {
            val email = edtEmail.text.toString()
            if (email.length < 6)
            {
                return "Email must be more than 6 characters!"
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                return "Email is invalid!"
            }

            if (email.lowercase() == "admin1@gmail.com" || email.lowercase() == "admin2@gmail.com")
            {
                return "Email already exists!"
            }

            val db = Database(requireActivity())
            if (db.checkStudentEmail(email.lowercase()))
            {
                    return "Email already exists!"
            }
            db.close()
            return null
        }

        // Detects whether the password is valid
        fun validPassword(): String? {
            val password = edtPassword.text.toString()
            if (password.length < 8)
            {
                return "Password must be more than 8 characters!"
            }

            return null
        }

        // Detects whether date of birth is valid
        fun validDateOfBirth(): String? {
            val dateOfBirth = txtDate.text.toString()
            if (dateOfBirth == "Date")
            {
                return "Date of Birth is invalid!"
            }

            return null
        }

        // Checks all field values submitted
        fun check(): Int {

            var check = 0

            val emailCheck = validEmail()
            val fullNameCheck = validFullName()
            val passwordCheck = validPassword()
            val dateCheck = validDateOfBirth()

            check = if (passwordCheck == null && fullNameCheck == null && emailCheck == null && dateCheck == null) {
                0
            } else {

                emailContainer.helperText = emailCheck
                fullNameContainer.helperText = fullNameCheck
                passwordContainer.helperText = passwordCheck
                txtDateError.text = dateCheck
                1
            }

            return check
        }

        // Used to Create or Edit Student Information
        fun createStudent() {

            val strFullName = edtFullName.text.toString()
            val strEmail = edtEmail.text.toString()
            val strPassword = edtPassword.text.toString()
            val strDateOfBirth = txtDate.text.toString()

            val check = check()
            if (check == 0) {

                try {
                    studentArray.add(StudentDataList(0, strFullName, strEmail.lowercase(), strPassword, strDateOfBirth, Global.currentTime))
                    Global.addStudentDB(requireActivity(), studentArray, Global.currentTime)

                    if (group == "Admin") {
                        Toast.makeText(activity,"Student Account Successfully Created", Toast.LENGTH_LONG).show()
                        Global.loadFragment(parentFragmentManager, R.id.flfragment_nav_drawer, studentsFragment)
                    } else {
                        Toast.makeText(activity,"Account Successfully Created", Toast.LENGTH_LONG).show()
                        Global.loadFragment(parentFragmentManager, R.id.flFragment, loginFragment)
                    }


                } catch (e:Exception){
                    e.printStackTrace()
                    Toast.makeText(activity,"An Error Occurred!", Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(activity,"One or more Fields are Invalid!", Toast.LENGTH_LONG).show()
            }
        }

        btnPickDate.setOnClickListener {
            Global.showDatePicker(requireActivity(), txtDate)
        }

        btnCreateStudent.setOnClickListener {
            createStudent()
        }

        // Sets Various values and exit buttons
        if (group == "Admin") {

            txtCreateStudent.text = "Adding Student"
            btnCreateStudent.text = "Create Student Account"
            btnCancelCreateStudent.text = "Cancel Account Creation"
            btnCancelCreateStudent.setOnClickListener {
                Global.loadFragment(parentFragmentManager, R.id.flfragment_nav_drawer, studentsFragment)
            }

        } else {
            btnCancelCreateStudent.setOnClickListener {
                Global.loadFragment(parentFragmentManager, R.id.flFragment, loginFragment)
            }
        }


    }
}