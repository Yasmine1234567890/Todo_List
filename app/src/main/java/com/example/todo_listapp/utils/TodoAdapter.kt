package com.example.todo_listapp.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_listapp.databinding.EachTodoItemBinding

class TodoAdapter(private val list: MutableList<TodoData>) :
    RecyclerView.Adapter<TodoAdapter.ToDoViewHolder>() {
    private var listener:ToDoAdapterClicksInterface?=null
    fun setlistener(listener:ToDoAdapterClicksInterface){
        this.listener=listener
    }


    inner class ToDoViewHolder(val binding: EachTodoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding =
            EachTodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.todoTask.text = this.task


                binding.editTask.setOnClickListener {
                    listener?.onEditItemClicked(this,position)
                }

                binding.deleteTask.setOnClickListener {
                    listener?.onDeleteTaskBtnClicked(this)
                }
            }
        }
    }
    interface ToDoAdapterClicksInterface{
        fun onDeleteTaskBtnClicked(todoData: TodoData)
        fun onEditItemClicked(toDoData: TodoData,position: Int)
    }
}
