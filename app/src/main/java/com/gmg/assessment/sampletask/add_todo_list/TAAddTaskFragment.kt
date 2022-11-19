package com.gmg.assessment.sampletask.add_todo_list

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.gmg.assessment.sampletask.common.TAAddTaskListener
import com.gmg.assessment.sampletask.common.TAConstants
import com.gmg.assessment.sampletask.common.TAUtil
import com.gmg.assessment.sampletask.databinding.TaFragmentAddTaskBinding


/**
 * The Fragment contains add task details
 */
class TAAddTaskFragment : DialogFragment() {

    companion object {
        const val TASK_NAME_ID : Int = 1
        const val TASK_DESCRIPTION_ID : Int = 2
        const val TASK_TYPE_ID : Int = 3
        const val TASK_SCHEDULE_DATE_ID : Int = 4
        const val IMAGE_FORMAT = "image/*"

        /**
         * The method called to create dialog fragment instance
         */
        fun newInstance() : TAAddTaskFragment {
            return TAAddTaskFragment()
        }
    }

    private lateinit var mContext : Context
    private lateinit var addTodoScreenBinding : TaFragmentAddTaskBinding
    private val addTaskViewModel by lazy { ViewModelProvider(this)[TAAddTaskViewModel::class.java] }
    private var addTaskListener : TAAddTaskListener? = null
    private var imagePickerResultCallBack: ActivityResultLauncher<Intent>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
        if (context is TAAddTaskListener) {
            addTaskListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        addTodoScreenBinding = TaFragmentAddTaskBinding.inflate(layoutInflater)
        return addTodoScreenBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            isCancelable = false
            setCancelable(false)
        }
    }

    private fun setDialogUI() {
        dialog?.window?.apply {
            val width = Resources.getSystem().displayMetrics.widthPixels - TAUtil.getDpValue(TAConstants.DIALOG_MARGIN)
            this.setLayout(width, ViewPager.LayoutParams.WRAP_CONTENT)
            this.setDimAmount(0.5f)
            this.setGravity(Gravity.CENTER)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogUI()
        observeViewModel()
        setListener()
        registerActivityResult()
    }

    /**
     * The method called to register activity result call back
     */
    private fun registerActivityResult() {
        imagePickerResultCallBack = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    addTodoScreenBinding.ivTaskImage.setImageURI(uri)
                    addTaskViewModel.uri = uri
                }
            }
        }
    }

    /**
     * The method called to set listener of view
     */
    private fun setListener() {
        with(addTodoScreenBinding) {
            etTaskName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //will be implement later
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let {
                        addTaskViewModel.taskName = it.toString()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    //will be implement later
                }
            })

            etTaskDescription.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //will be implement later
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let {
                        addTaskViewModel.taskDescription = it.toString()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    //will be implement later
                }
            })

            etTaskType.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //will be implement later
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let {
                        addTaskViewModel.taskType = it.toString()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    //will be implement later
                }
            })

            etTaskScheduleDate.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //will be implement later
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let {
                        addTaskViewModel.taskScheduleDate= it.toString()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    //will be implement later
                }
            })

            btnSave.setOnClickListener {
                addTaskViewModel.isTodoDataValid(mContext)
            }

            btnCancel.setOnClickListener {
                dialog?.dismiss()
            }

            ivTaskImage.setOnClickListener {
                val captureIntent = Intent(Intent.ACTION_GET_CONTENT)
                captureIntent.type = IMAGE_FORMAT
                imagePickerResultCallBack?.launch(captureIntent)
            }
        }
    }

    /**
     * The method called to observe view model
     */
    private fun observeViewModel() {
        with(addTaskViewModel) {
            editFieldError.observe(this@TAAddTaskFragment) { fieldError ->
                fieldError?.let {
                    when (it.fieldId) {
                        TASK_DESCRIPTION_ID -> addTodoScreenBinding.etTaskDescription.error = getString(it.stringIdResource)
                        TASK_NAME_ID -> addTodoScreenBinding.etTaskName.error = getString(it.stringIdResource)
                        TASK_TYPE_ID-> addTodoScreenBinding.etTaskType.error = getString(it.stringIdResource)
                        TASK_SCHEDULE_DATE_ID -> addTodoScreenBinding.etTaskScheduleDate.error = getString(it.stringIdResource)
                    }
                }
            }

            isTodoSaveSuccessfully.observe(this@TAAddTaskFragment) {
                if (it == true) {
                    dialog?.dismiss()
                    addTaskListener?.onTaskAddedSuccessfully()
                }
            }
        }
    }
}