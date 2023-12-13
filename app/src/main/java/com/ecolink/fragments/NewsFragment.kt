// NewsFragment.kt
package com.ecolink.fragments

import DetailFragment
import com.ecolink.adapters.LessonAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecolink.api.Api
import com.ecolink.R
import com.ecolink.databinding.FragmentNewsBinding
import com.ecolink.models.Lesson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private lateinit var newsListAdapter: LessonAdapter
    private var lessonList: MutableList<Lesson> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        setupRecyclerView()

        val apiInterface = Api.create()

        apiInterface.allproject().enqueue(object : Callback<List<Lesson>> {
            override fun onResponse(call: Call<List<Lesson>>, response: Response<List<Lesson>>) {
                if (isAdded && activity != null) {
                    if (response.isSuccessful) {
                        val newsListResponse = response.body()
                        if (newsListResponse != null) {
                            updateUI(newsListResponse)
                        } else {
                            showToast("Null response body")
                        }
                    } else {
                        showToast("Unsuccessful response: ${response.code()}")
                        Log.e("API_ERROR", response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }

            override fun onFailure(call: Call<List<Lesson>>, t: Throwable) {
                if (isAdded && activity != null) {
                    showToast("Network failure: ${t.message}")
                    Log.e("API_ERROR", "Network failure: ${t.message}", t)
                }
            }
        })

        return binding.root
    }

    private fun showToast(message: String) {
        if (isAdded && context != null) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(newsListResponse: List<Lesson>) {
        lessonList.clear()
        lessonList.addAll(newsListResponse)
        newsListAdapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        newsListAdapter = LessonAdapter(lessonList, object : LessonAdapter.OnItemClickListener {
            override fun onItemClick(lesson: Lesson) {
                val detailFragment = DetailFragment()

                // Check if the fragment is already added to the fragment manager
                val existingDetailFragment =
                    requireActivity().supportFragmentManager.findFragmentByTag("DetailFragment") as DetailFragment?

                if (existingDetailFragment != null) {
                    // Fragment is already added, update the selected lesson
                    existingDetailFragment.setSelectedLesson(lesson)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, existingDetailFragment, "DetailFragment")
                        .commit()
                } else {
                    // Fragment is not added, set the selected lesson and add it to the fragment manager
                    detailFragment.setSelectedLesson(lesson)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, detailFragment, "DetailFragment")
                        .addToBackStack(null)
                        .commit()
                }
            }

            override fun onDeleteClick(lesson: Lesson) {
                // Handle delete button click
                deleteLesson(lesson.id)
            }
        })

        binding.rvNews.adapter = newsListAdapter
        binding.rvNews.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun deleteLesson(lessonId: String) {
        // Implement the logic to delete the lesson
        // You can use Retrofit or any other method to make the API call for deletion
    }
}
