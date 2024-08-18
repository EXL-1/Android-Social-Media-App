package com.example.a22806287.post

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.a22806287.Global
import com.example.a22806287.PostDataList
import com.example.a22806287.R
import com.example.a22806287.comment.CommentPostFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

private val postArray = ArrayList<PostDataList>()

// Edit Add Post Fragment
class EditAddPostFragment : Fragment(R.layout.fragment_edit_add_post) {
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Defining Values / Shared Preferences / Fragments
        val sp = activity?.getSharedPreferences("Application_info", Context.MODE_PRIVATE)
        val spStudentID = sp?.getString("userID", "")

        val args = this@EditAddPostFragment.arguments
        val mode = args?.getString("MODE")

        val bundlePostID = args?.getInt("POST_ID")

        val txtEditAddPost : TextView = view.findViewById(R.id.txtEditAddPost)
        val edtPostTitle : EditText = view.findViewById(R.id.edtPostTitle)
        val edtPostDescription : EditText = view.findViewById(R.id.edtPostDescription)

        val btnSavePost : FloatingActionButton = view.findViewById(R.id.btnSavePost)
        val btnExitAddEditPost : FloatingActionButton = view.findViewById(R.id.btnExitAddEditPost)

        val commentPostFragment = CommentPostFragment()
        val postsFragment = PostsFragment()

        // Used to Return to the Previous Post
        fun returnToPreviousPost() {
            val bundle = Bundle()
            commentPostFragment.arguments = bundle

            if (bundlePostID != null) {
                bundle.putInt("POST_ID", bundlePostID)
            }

            Global.loadFragment(parentFragmentManager, R.id.flfragment_nav_drawer, commentPostFragment)
        }

        // Used to Create or Edit Post Information
        fun editCreatePost() {

            val strPostTitle = edtPostTitle.text.toString()
            val strPostDescription = edtPostDescription.text.toString()

            val check = Global.isEmpty(requireActivity(), strPostTitle, strPostDescription)

            if (check == 0) {

                try {
                    if (mode == "Editing") {

                        if (bundlePostID != null) {
                            postArray.add(PostDataList(bundlePostID.toInt(), 0, strPostTitle, strPostDescription, Global.currentTime))
                            Global.updatePostDB(requireActivity(), postArray, Global.currentTime)
                            returnToPreviousPost()

                            postArray.clear()
                        }

                    } else {
                        if (spStudentID != null) {
                            postArray.add(PostDataList(0, spStudentID.toInt(), strPostTitle, strPostDescription, Global.currentTime))
                        }

                        Global.addPostDB(requireActivity(), postArray, Global.currentTime)
                        Toast.makeText(activity, "Post Successfully Created", Toast.LENGTH_LONG)
                            .show()
                        Global.loadFragment(
                            parentFragmentManager,
                            R.id.flfragment_nav_drawer,
                            postsFragment
                        )

                        postArray.clear()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(activity, "An Error Occurred!", Toast.LENGTH_LONG).show()
                }
            }
        }

        btnSavePost.setOnClickListener {
            editCreatePost()
        }

        // Sets Exit Buttons and Prefilled values
        if (mode == "Editing") {

            if (bundlePostID != null) {
                val post = Global.getPostDB(requireActivity(), bundlePostID)

                txtEditAddPost.text =  "Editing Post"
                edtPostTitle.setText(post[0].postTitle)
                edtPostDescription.setText(post[0].postDescription)
            }

            btnExitAddEditPost.setOnClickListener {
                returnToPreviousPost()
            }

        } else {
            btnExitAddEditPost.setOnClickListener {
                Global.loadFragment(parentFragmentManager, R.id.flfragment_nav_drawer, postsFragment)
            }
        }

        args?.clear()
    }
}