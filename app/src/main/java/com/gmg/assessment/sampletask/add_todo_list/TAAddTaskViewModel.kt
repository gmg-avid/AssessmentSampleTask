package com.gmg.assessment.sampletask.add_todo_list

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gmg.assessment.sampletask.R
import com.gmg.assessment.sampletask.common.TAConstants
import com.gmg.assessment.sampletask.model.TAFieldError
import com.gmg.assessment.sampletask.model.TATodoTask
import com.gmg.assessment.sampletask.repository.TARepository
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * Created by gowtham.muralivel on 18/11/22.
 */
class TAAddTaskViewModel(application: Application) : AndroidViewModel(application) {

    var taskName : String? = null
    var taskDescription : String? = null
    var taskScheduleDate : String? = null
    var taskType : String? = null
    var uri : Uri? = null
    val editFieldError : MutableLiveData<TAFieldError> = MutableLiveData()
    val isTodoSaveSuccessfully : MutableLiveData<Boolean> = MutableLiveData()
    private val currentUserEmail by lazy { TARepository.getCurrentUserEmail() }
    private val TAG = TAAddTaskViewModel::class.java.simpleName

    fun isTodoDataValid(context: Context) {
        var isFieldAreValid = true
        if (taskName.isNullOrBlank()) {
            editFieldError.value = TAFieldError(R.string.task_name_should_not_empty, TAAddTaskFragment.TASK_NAME_ID)
            isFieldAreValid = false
        }
        if (taskDescription.isNullOrBlank()) {
            editFieldError.value = TAFieldError(R.string.task_description_should_not_empty, TAAddTaskFragment.TASK_DESCRIPTION_ID)
            isFieldAreValid = false
        }
        if (taskScheduleDate.isNullOrBlank()) {
            editFieldError.value = TAFieldError(R.string.task_schedule_date_should_not_empty, TAAddTaskFragment.TASK_SCHEDULE_DATE_ID)
            isFieldAreValid = false
        }
        if (taskType.isNullOrBlank()) {
            editFieldError.value = TAFieldError(R.string.task_type_should_not_empty, TAAddTaskFragment.TASK_TYPE_ID)
            isFieldAreValid = false
        }
        if (isFieldAreValid && !currentUserEmail.isNullOrBlank()) {
            val imagePath = if (uri != null) {
                savePhoto(uri!!, context)
            } else {
                null
            }
            saveTodoData(TATodoTask(taskName!!, taskDescription!!, taskScheduleDate!!, taskType!!, imagePath), currentUserEmail!!)
        }
    }

    private fun saveTodoData(todoTask: TATodoTask, currentUserEmail: String) {
        TARepository.addTodoTaskList(todoTask, currentUserEmail)
        isTodoSaveSuccessfully.value = true
    }

    private fun savePhoto(photoUri: Uri, context: Context): String? {
        try {
            File(context.filesDir.absolutePath, TAConstants.TASK_NAME).let { file ->
                if (!file.exists()) {
                    file.mkdirs()
                }
                val fileName = System.currentTimeMillis().toString() + TAConstants.JPEG
                val imageFile = File(file.absolutePath, fileName)
                imageFile.createNewFile()
                context.contentResolver.openInputStream(photoUri)?.let {
                    var outputStream: OutputStream? = null
                    try {
                        outputStream = FileOutputStream(imageFile)
                        val buf = ByteArray(1024)
                        var len: Int
                        while (it.read(buf).also { len = it } > 0) {
                            outputStream.write(buf, 0, len)
                        }
                    } catch (exception: java.lang.Exception) {
                        Log.e(TAG, exception.toString())
                    } finally {
                       it.close()
                       outputStream?.close()
                    }
                }
                return imageFile.absolutePath
            }
        } catch (exception : Exception) {
            Log.e(TAG, exception.toString())
        }
        return null
    }
}