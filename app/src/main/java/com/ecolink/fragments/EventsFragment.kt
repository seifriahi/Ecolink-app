package com.ecolink.fragments
import NewsAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecolink.api.Api
import com.ecolink.databinding.FragmentNews1Binding
import com.ecolink.models.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsFragment : Fragment() {

    private lateinit var binding: FragmentNews1Binding
    private lateinit var newsListAdapter: NewsAdapter
    private var newsList: MutableList<News> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNews1Binding.inflate(inflater, container, false)
        setupRecyclerView()

        val apiInterface = Api.create()

        apiInterface.allprojects().enqueue(object : Callback<List<News>> {
            override fun onResponse(call: Call<List<News>>, response: Response<List<News>>) {
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

            override fun onFailure(call: Call<List<News>>, t: Throwable) {
                showToast("Network failure: ${t.message}")
                Log.e("API_ERROR", "Network failure: ${t.message}", t)
            }
        })

        return binding.root
    }

    private fun updateUI(newsListResponse: List<News>) {
        newsList.clear()
        newsList.addAll(newsListResponse)
        newsListAdapter.notifyDataSetChanged()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setupRecyclerView() {
        newsListAdapter = NewsAdapter(newsList)
        binding.rvNews.adapter = newsListAdapter
        binding.rvNews.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}
