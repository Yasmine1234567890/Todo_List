package com.example.todo_listapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo_listapp.R
import com.example.todo_listapp.databinding.FragmentHomeBinding
import com.example.todo_listapp.utils.TodoAdapter
import com.example.todo_listapp.utils.TodoData
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.todo_listapp.utils.TodoAdapter.ToDoAdapterClicksInterface




class HomeFragment : Fragment(), AddtodoPopupFragment.DialogNextBtnClickListener,
    TodoAdapter.ToDoAdapterClicksInterface {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private var popupFragment: AddtodoPopupFragment? =null
    private lateinit var adapter: TodoAdapter
    private lateinit var mlist: MutableList<TodoData>





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        getDataFromFirebase()
        registerEvents()


    }

    private fun init(view: View) {

        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef=FirebaseDatabase.getInstance().reference
            .child("Tasks").child(auth.currentUser?.uid.toString())

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager=LinearLayoutManager(context)
        mlist= mutableListOf()
        adapter=TodoAdapter(mlist)
        adapter.setlistener(this)
        binding.recyclerView.adapter=adapter

    }

    private fun getDataFromFirebase(){
        databaseRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mlist.clear()
                for (taskSnapshot in snapshot.children){
                    val todoTask=taskSnapshot.key?.let {
                        TodoData(it, taskSnapshot.value.toString())
                    }
                    if (todoTask!=null){
                        mlist.add(todoTask)

                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show()
            }


        })
    }
    private fun registerEvents(){
        binding.addBtnHome.setOnClickListener {
            if(popupFragment!=null){
                childFragmentManager.beginTransaction().remove(popupFragment!!).commit()
            }
            popupFragment=AddtodoPopupFragment()
            popupFragment!!.setListener(this)
            popupFragment!!.show(
                childFragmentManager,
                AddtodoPopupFragment.TAG


            )



        }
    }

    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {
        databaseRef.push().setValue(todo).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context,"Todo saved successfully !!",Toast.LENGTH_SHORT).show()


            }else{
                Toast.makeText(context,task.exception?.message,Toast.LENGTH_SHORT).show()
            }
            todoEt.text=null
            popupFragment!!.dismiss()
        }

    }

    override fun onUpdateTask(toDoData: TodoData, todoEt: TextInputEditText) {
        val map=HashMap<String,Any>()
        map[toDoData.taskId]=toDoData.task
        databaseRef.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context,"Updated successfully",Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
            }
            todoEt.text=null
            popupFragment!!.dismiss()
        }
    }

    override fun onDeleteTaskBtnClicked(todoData: TodoData) {
        databaseRef.child(todoData.taskId).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditItemClicked(toDoData: TodoData, position: Int) {
        if (popupFragment !=null)
            childFragmentManager.beginTransaction().remove(popupFragment!!).commit()

        popupFragment=AddtodoPopupFragment.newInstance(toDoData.taskId,toDoData.task)
        popupFragment!!.setListener(this)
        popupFragment!!.show(childFragmentManager,AddtodoPopupFragment.TAG)


        }

}



