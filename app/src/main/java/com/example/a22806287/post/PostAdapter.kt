package com.example.a22806287.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a22806287.Database
import com.example.a22806287.Global
import com.example.a22806287.PostDataList
import com.example.a22806287.R
import com.example.a22806287.comment.CommentPostFragment

// Post Recycler View
class PostAdapter (
    private val posts: ArrayList<PostDataList>,
    private val db: Database,
    private val fragmentManager: FragmentManager
    ) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

        // Defining Values
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val txtPostTitle : TextView = view.findViewById(R.id.txtPostTitle)
            val txtPostDescription : TextView = view.findViewById(R.id.txtPostDescription)
        }

        // Inflating our Layout
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.post_layout, parent, false)
            return ViewHolder(view)
        }

        // Used for each individual Post Layout
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val post = posts[position]
            holder.txtPostTitle.text = post.postTitle
            holder.txtPostDescription.text = post.postDescription

            val bundle = Bundle()
            val commentPostFragment = CommentPostFragment()

            // View Button Post
            holder.txtPostTitle.setOnClickListener {

                bundle.putInt("POST_ID", post.postID)
                bundle.putString("DATE_POSTED", post.datePosted)

                commentPostFragment.arguments = bundle
                Global.loadFragment(fragmentManager, R.id.flfragment_nav_drawer, commentPostFragment)
            }

            // View Button Post
            holder.txtPostDescription.setOnClickListener {

                bundle.putInt("POST_ID", post.postID)
                bundle.putString("DATE_POSTED", post.datePosted)

                commentPostFragment.arguments = bundle
                Global.loadFragment(fragmentManager, R.id.flfragment_nav_drawer, commentPostFragment)

            }
        }

        // Get the amount of Posts
        override fun getItemCount() = posts.size

    }