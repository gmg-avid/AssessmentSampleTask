package com.gmg.assessment.sampletask.todo_list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmg.assessment.sampletask.add_todo_list.TAAddTaskFragment
import com.gmg.assessment.sampletask.common.TAAddTaskListener
import com.gmg.assessment.sampletask.databinding.TaActivityTodoListBinding
import com.gmg.assessment.sampletask.login.TALoginActivity
import com.gmg.assessment.sampletask.model.TATodoTask


/**
 * Created by gowtham.muralivel on 06/11/22.
 */
class TATodoListActivity : AppCompatActivity(), TAAddTaskListener {

    private lateinit var todoListActivityBinding : TaActivityTodoListBinding
    private lateinit var todoListAdapter : TATodoListAdapter
    private val todoListViewModel : TATodoListViewModel by lazy {  ViewModelProvider(this)[TATodoListViewModel::class.java] }
    val TAG : String by lazy { TATodoListActivity::class.java.simpleName }

    companion object {
        const val TODO_ADD_DIALOG_TAG = "todo_add_dialog_tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoListActivityBinding = TaActivityTodoListBinding.inflate(layoutInflater)
        setContentView(todoListActivityBinding.root)
        setListener()
        observeViewModel()
        initViewModel()
    }

    private fun initViewModel() {
        todoListViewModel.apply {
            fetchInitialData()
        }
    }

    private fun setAdapter(todoTaskList : List<TATodoTask>) {
        todoListActivityBinding.apply {
            if (todoTaskList.isEmpty()) {
                tvTaskUnavailable.visibility = View.VISIBLE
                rvTodoList.visibility = View.GONE
            } else {
                tvTaskUnavailable.visibility = View.GONE
                rvTodoList.visibility = View.VISIBLE
                if (this@TATodoListActivity::todoListAdapter.isInitialized) {
                    todoListAdapter.updateTodoList(todoTaskList)
                } else {
                    todoListAdapter = TATodoListAdapter(this@TATodoListActivity, todoListViewModel.todoTaskList)
                    rvTodoList.adapter = todoListAdapter
                }
            }
        }
    }

    private fun observeViewModel() {
        todoListViewModel.apply {
            isUserEmailUpdate.observe(this@TATodoListActivity) {
                if (it != null) {
                    todoListActivityBinding.tvUserName.text = it
                }
            }

            isTaskDetailUpdated.observe(this@TATodoListActivity) {
                if (it == true) {
                    setAdapter(todoTaskList)
                }
             }
        }
    }

    private fun setListener() {
        todoListActivityBinding.apply {
            rvTodoList.layoutManager = GridLayoutManager(this@TATodoListActivity, 2)
            tvLogout.setOnClickListener {
                todoListViewModel.logoutUser()
                val intent = Intent(this@TATodoListActivity, TALoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

            fabAddTask.setOnClickListener {
                showAddTaskDialog()
            }
        }
    }

    private fun showAddTaskDialog() {
        try {
            val fragmentManager: FragmentManager = supportFragmentManager
            fragmentManager.executePendingTransactions()
            val fragment = fragmentManager.findFragmentByTag(TODO_ADD_DIALOG_TAG)
            if (fragment != null && fragment.isAdded) {
                // Already showing the alert dialog
            } else {
               val alertDialogFragment = TAAddTaskFragment.newInstance()
                alertDialogFragment.show(fragmentManager, TODO_ADD_DIALOG_TAG)
            }
        } catch (exception: Exception) {
            //Execution of pending transaction is will happen, it is not crash exception
            Log.e(TAG, exception.toString())
        }
    }

    override fun onTaskAddedSuccessfully() {
        todoListViewModel.getTaskList()
    }
}