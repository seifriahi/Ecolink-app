// LessonAdapter.kt
package com.ecolink.adapters
import coil.load
import com.bumptech.glide.Glide

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ecolink.R
import com.ecolink.databinding.SingleItemNewsBinding
import com.ecolink.models.Lesson

class LessonAdapter(
    private val lessonList: List<Lesson>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(lesson: Lesson)
        fun onDeleteClick(lesson: Lesson)
    }

    inner class LessonViewHolder(private val binding: SingleItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.actionShowMore.setOnClickListener(this)
        }

        fun bind(lesson: Lesson) {
            Glide.with(binding.root.context)
                .load("https://ecolink.onrender.com/images/${lesson.imageRes}")
                .into(binding.newsImage)

            binding.newsTitle.text = lesson.title
            binding.newsDescription.text = lesson.description
        }

        override fun onClick(view: View) {
            when (view.id) {
                R.id.actionShowMore -> itemClickListener.onDeleteClick(lessonList[adapterPosition])
                else -> itemClickListener.onItemClick(lessonList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SingleItemNewsBinding.inflate(inflater, parent, false)
        return LessonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(lessonList[position])
    }

    override fun getItemCount(): Int = lessonList.size
}
