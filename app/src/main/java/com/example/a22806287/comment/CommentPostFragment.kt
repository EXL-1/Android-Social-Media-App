package com.example.a22806287.comment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a22806287.CommentDataList
import com.example.a22806287.Database
import com.example.a22806287.Global
import com.example.a22806287.R
import com.example.a22806287.post.EditAddPostFragment
import com.example.a22806287.post.PostsFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

// CommentPost Fragment
class CommentPostFragment : Fragment(R.layout.fragment_comment_post) {

    private val commentArray = ArrayList<CommentDataList>()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Defining Values / Shared Preferences / Fragments
        val sp = activity?.getSharedPreferences("Application_info", Context.MODE_PRIVATE)
        val spStudentID = sp?.getString("userID", "")

        val args = this@CommentPostFragment.arguments
        val bundlePostID = args?.getInt("POST_ID")

        val bundle = Bundle()

        val txtPostTitle : TextView = view.findViewById(R.id.txtPostTitle)
        val txtPostDescription : TextView = view.findViewById(R.id.txtPostDescription)
        val txtPostByUser : TextView = view.findViewById(R.id.txtPostByUser)
        val txtDatePosted : TextView = view.findViewById(R.id.txtPostDate)

        val edtComment : EditText = view.findViewById(R.id.edtSendComment)
        val btnEditPost : FloatingActionButton = view.findViewById(R.id.btnEditPost)
        val sendComment : FloatingActionButton = view.findViewById(R.id.btnSendComment)
        val exitPost : FloatingActionButton = view.findViewById(R.id.btnExitPost)

        val commentPostFragment = CommentPostFragment()
        val editAddPostFragment = EditAddPostFragment()
        val postsFragment = PostsFragment()

        val db = Database(requireActivity())

        // Setting the Values for Posts and Option of Editing Posts
        if (bundlePostID != null) {
            val post = Global.getPostDB(requireActivity(), bundlePostID)
            val student = Global.getStudentByIDDB(requireActivity(), post[0].studentID)

            txtPostTitle.text = post[0].postTitle
            txtPostDescription.text = post[0].postDescription
            txtPostByUser.text = "By " +student[0].fullName
            txtDatePosted.text = "Date Posted: " + post[0].datePosted

            if (spStudentID?.toInt() == post[0].studentID) {

                btnEditPost.setOnClickListener {

                    bundle.putInt("POST_ID", post[0].postID)
                    bundle.putString("MODE", "Editing")
                    editAddPostFragment.arguments = bundle

                    Global.loadFragment(parentFragmentManager, R.id.flfragment_nav_drawer, editAddPostFragment)
                }

            } else {
                btnEditPost.alpha = 0.0f
            }
        }

        // Get all Comments
        fun getAllComments(): ArrayList<CommentDataList> {
            val commentsList = ArrayList<CommentDataList>()

            if (bundlePostID != null) {
                val cursor = db.getAllMessagesPost("comment", bundlePostID)

                if (cursor.moveToFirst()) {
                    do {
                        val studentId =
                            cursor.getInt(cursor.getColumnIndexOrThrow(Database.STUDENT_ID_COL))
                        val postId =
                            cursor.getInt(cursor.getColumnIndexOrThrow(Database.POST_ID_COL))
                        val comment =
                            cursor.getString(cursor.getColumnIndexOrThrow(Database.COMMENT_COL))
                        val datePosted =
                            cursor.getString(cursor.getColumnIndexOrThrow(Database.DATE_CREATED_COL))

                        val commentInfo = CommentDataList(0, postId, studentId, comment, datePosted)
                        commentsList.add(commentInfo)
                    } while (cursor.moveToNext())
                }
                cursor.close()
                db.close()
            }
            return commentsList
        }

        // Defining the Comment Recycler View Adapter
        val rcComments = view.findViewById<RecyclerView>(R.id.rcComments);
        rcComments.layoutManager = LinearLayoutManager(activity);
        val commentAdapter = CommentAdapter(getAllComments(), db, parentFragmentManager, requireActivity())
        rcComments.adapter = commentAdapter

        // Used to send a Comment to a Post
        sendComment.setOnClickListener {

            val strComment = edtComment.text.toString()
            val check = Global.isValueEmpty(requireActivity(), strComment)

            if (check == 0) {

                try {
                    if (bundlePostID != null) {
                        val post = Global.getPostDB(requireActivity(), bundlePostID)

                        if (spStudentID != null) {
                            commentArray.add(CommentDataList(0, post[0].postID, spStudentID.toInt(), strComment, Global.currentTime))
                        }

                        Global.addCommentDB(requireActivity(), commentArray)

                        bundle.putInt("POST_ID", post[0].postID)
                        commentPostFragment.arguments = bundle

                        Global.loadFragment(parentFragmentManager, R.id.flfragment_nav_drawer, commentPostFragment)

                        Toast.makeText(activity, "Message Successfully Sent!", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(activity, "An Error Occurred!", Toast.LENGTH_LONG).show()
                }
            }
        }

        exitPost.setOnClickListener {
            Global.loadFragment(parentFragmentManager, R.id.flfragment_nav_drawer, postsFragment)
        }

        args?.clear()
    }
}