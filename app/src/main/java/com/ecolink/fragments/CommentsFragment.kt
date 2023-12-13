package com.ecolink.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecolink.R
import com.ecolink.adapters.CommentAdapter
import com.ecolink.api.Api
import com.ecolink.models.Comment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val commentsAdapter = CommentAdapter()

    private lateinit var lessonId: String

    companion object {
        private const val ARG_LESSON_ID = "lessonId"

        fun newInstance(lessonId: String): CommentsFragment {
            val fragment = CommentsFragment()
            val args = Bundle()
            args.putString(ARG_LESSON_ID, lessonId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvComments)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = commentsAdapter

        val lessonId = arguments?.getString(ARG_LESSON_ID)
        if (!lessonId.isNullOrBlank()) {
            fetchComments(lessonId)
        }
    }

    private fun fetchComments(lessonId: String) {
        val apiInterface = Api.create()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = apiInterface.getCommentsByLessonId(lessonId)
                if (response.isSuccessful) {
                    val comments = response.body()
                    if (comments != null) {
                        displayComments(comments)
                    }
                }
            } catch (e: Exception) {
                // Handle failure
            }
        }
    }

    private fun displayComments(comments: List<Comment>) {
        commentsAdapter.submitList(comments)
    }
}
