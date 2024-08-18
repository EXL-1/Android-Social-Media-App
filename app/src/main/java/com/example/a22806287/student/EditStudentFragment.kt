package com.example.a22806287.student

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.a22806287.Database
import com.example.a22806287.Global
import com.example.a22806287.R
import com.example.a22806287.StudentDataList
import com.example.a22806287.post.PostsFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class EditStudentFragment : Fragment(R.layout.fragment_edit_student) {

    private val studentArray = ArrayList<StudentDataList>()

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Defining Values / Shared Preferences / Fragments
        val sp = activity?.getSharedPreferences("Application_info", Context.MODE_PRIVATE)
        val editor = sp?.edit();

        val group = sp?.getString("group", "")
        val spStudentID = sp?.getString("userID", "")
        val spEmail = sp?.getString("userEmail", "")

        val args = this@EditStudentFragment.arguments
        val bundleStudentID = args?.getString("STUDENT_ID")
        val bundleStudentEmail = args?.getString("STUDENT_EMAIL")

        val fullNameContainer : TextInputLayout = view.findViewById(R.id.fullNameContainer)
        val emailContainer : TextInputLayout = view.findViewById(R.id.emailContainer)
        val passwordContainer : TextInputLayout = view.findViewById(R.id.passwordContainer)

        val edtFullName : TextInputEditText = view.findViewById(R.id.edtFullName)
        val edtEmail : TextInputEditText = view.findViewById(R.id.edtNewEmail)
        val edtPassword : TextInputEditText = view.findViewById(R.id.edtNewPassword)

        val txtEditStudent = view.findViewById<TextView>(R.id.txtEditStudent)
        val txtDate : TextView = view.findViewById(R.id.txtDate)
        val txtDateCreated : TextView = view.findViewById(R.id.txtDateCreated)
        val txtDateError : TextView = view.findViewById(R.id.txtDateError)
        val btnPickDate : Button = view.findViewById(R.id.btnPickDate)
        val btnEditStudent : Button = view.findViewById(R.id.btnEditStudent)
        val btnCancelEditStudent : Button = view.findViewById(R.id.btnCancelEditStudent)

        val studentsFragment = StudentsFragment()
        val postsFragment = PostsFragment()

        // Updates Values for Shared Preference
        fun updateSharedPreference(studentFullName : String, strEmail : String, strPassword : String, studentDOB : String) {
            editor?.putString("userName", studentFullName)
            editor?.putString("userEmail", strEmail)
            editor?.putString("userPassword", strPassword)
            editor?.putString("userDOB", studentDOB)
            editor?.apply()
        }

        // Prefills Text Field Information
        @SuppressLint("SetTextI18n")
        fun fillInformationFields(name : String, email : String, password : String, dob : String, dateCreated : String) {
            fullNameContainer.editText?.setText(name)
            emailContainer.editText?.setText(email)
            passwordContainer.editText?.setText(password)
            txtDate.text = dob
            txtDateCreated.text = "Account Created: $dateCreated"
        }

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
                return if ((spEmail == email) || (bundleStudentEmail == email)) {
                    null
                } else {
                    "Email already exists!"
                }

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

        // Used to Edit Student Information
        fun editStudent() {

            val strFullName = edtFullName.text.toString()
            val strEmail = edtEmail.text.toString()
            val strPassword = edtPassword.text.toString()
            val strDateOfBirth = txtDate.text.toString()

            val check = check()
            if (check == 0) {

                try {

                    if (group == "Admin") {

                        if (bundleStudentID != null) {
                            studentArray.add(StudentDataList(bundleStudentID.toInt(), strFullName, strEmail.lowercase(), strPassword, strDateOfBirth, Global.currentTime))
                        }
                        Toast.makeText(activity,"Student Information Successfully Updated", Toast.LENGTH_LONG).show()
                        Global.loadFragment(parentFragmentManager, R.id.flfragment_nav_drawer, studentsFragment)

                    } else {

                        if (spStudentID != null) {
                            studentArray.add(StudentDataList(spStudentID.toInt(), strFullName, strEmail.lowercase(), strPassword, strDateOfBirth, Global.currentTime))
                            updateSharedPreference(strFullName, strEmail.lowercase(), strPassword, strDateOfBirth)
                        }

                        Global.loadFragment(parentFragmentManager, R.id.flfragment_nav_drawer, postsFragment)
                        Toast.makeText(activity,"Profile Successfully Updated", Toast.LENGTH_LONG).show()
                    }

                    Global.updateStudentDB(requireActivity(), studentArray, Global.currentTime)

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

        btnEditStudent.setOnClickListener {
            editStudent()
        }

        // Sets Various Information Values and Exit Buttons
        if (group == "Admin") {
            txtEditStudent.text = "Student Information"
            btnCancelEditStudent.text = "Return to Students Menu"

            btnCancelEditStudent.setOnClickListener {
                Global.loadFragment(parentFragmentManager, R.id.flfragment_nav_drawer, studentsFragment)
            }

            if (bundleStudentEmail != null) {
                val s = Global.getStudentDB(requireActivity(), bundleStudentEmail)
                fillInformationFields(s[0].fullName, s[0].email, s[0].password, s[0].dob, s[0].dateCreated)
            }

        } else {

            if (spEmail != null) {
                val s = Global.getStudentDB(requireActivity(), spEmail)
                fillInformationFields(s[0].fullName, s[0].email, s[0].password, s[0].dob, s[0].dateCreated)
            }

            btnCancelEditStudent.setOnClickListener {
                Global.loadFragment(parentFragmentManager, R.id.flfragment_nav_drawer, postsFragment)
            }
        }

        args?.clear()
    }
}