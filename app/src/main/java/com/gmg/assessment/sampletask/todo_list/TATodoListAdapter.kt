package com.gmg.assessment.sampletask.todo_list

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gmg.assessment.sampletask.R
import com.gmg.assessment.sampletask.databinding.TaAdapterTodoCellBinding
import com.gmg.assessment.sampletask.model.TATodoTask

/**
 * Created by gowtham.muralivel on 13/11/22.
 */
class TATodoListAdapter(val context: Context, var todoTaskList : List<TATodoTask> = mutableListOf()) : RecyclerView.Adapter<TATodoListAdapter.TATodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TATodoViewHolder {
        TaAdapterTodoCellBinding.inflate(LayoutInflater.from(parent.context), parent, false).let {
            return TATodoViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: TATodoViewHolder, position: Int) {
        val adapterPosition = holder.adapterPosition
        if (adapterPosition < todoTaskList.size) {
            val todoTask = todoTaskList[adapterPosition]
            holder.todoCellBinding.apply {
                tvTaskDate.text = todoTask.taskDate
                tvTaskDescription.text = todoTask.taskDescription
                tvTaskTitle.text = todoTask.taskName
                tvTaskType.text = todoTask.taskType
                todoTask.imagePath?.let {
                    Glide.with(context)
                        .load(it)
                        .error(R.drawable.ic_todo_logo)
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                        .skipMemoryCache(true)
                        .into(ivTaskImage)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return todoTaskList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTodoList(todoTaskList: List<TATodoTask>) {
        this.todoTaskList = todoTaskList
        notifyDataSetChanged()
    }

    inner class TATodoViewHolder(val todoCellBinding: TaAdapterTodoCellBinding) : RecyclerView.ViewHolder(todoCellBinding.root)
}