package com.example.a22806287.post

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a22806287.Database
import com.example.a22806287.Global
import com.example.a22806287.PostDataList
import com.example.a22806287.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

// Posts Fragment
class PostsFragment : Fragment(R.layout.fragment_posts) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Defining Values / Fragments
        val btnAddPost : FloatingActionButton = view.findViewById(R.id.btnAddPost)

        //val editAddPostFragment
        val db = Database(requireActivity())

        val editAddPostFragment = EditAddPostFragment()

        // Get all Posts
        fun getPosts(): ArrayList<PostDataList> {
            val postsList = ArrayList<PostDataList>()
            val cursor = db.getAll("post")

            if (cursor.moveToFirst()) {
                do {
                    val studentId = cursor.getInt(cursor.getColumnIndexOrThrow(Database.STUDENT_ID_COL))
                    val postId = cursor.getInt(cursor.getColumnIndexOrThrow(Database.POST_ID_COL))
                    val postTitle = cursor.getString(cursor.getColumnIndexOrThrow(Database.POST_TITLE_COL))
                    val postDescription = cursor.getString(cursor.getColumnIndexOrThrow(Database.POST_DESCRIPTION_COL))
                    val postDate = cursor.getString(cursor.getColumnIndexOrThrow(Database.DATE_CREATED_COL))

                    val postInfo = PostDataList(postId, studentId, postTitle, postDescription, postDate)
                    postsList.add(postInfo)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()

            return postsList
        }

        // Defining the Posts Recycler View Adapter
        val rcPosts = view.findViewById<RecyclerView>(R.id.rcPosts);
        rcPosts.layoutManager = LinearLayoutManager(activity);
        val postAdapter = PostAdapter(getPosts(), db, parentFragmentManager)
        rcPosts.adapter = postAdapter

        btnAddPost.setOnClickListener {
            Global.loadFragment(parentFragmentManager, R.id.flfragment_nav_drawer, editAddPostFragment)
        }

    }
}