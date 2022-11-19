package com.gmg.assessment.sampletask.common

import android.app.Application
import com.gmg.assessment.sampletask.repository.local.TAPreferences

/**
 * The application instance of the class
 */
class TAApplication : Application() {

    companion object {
        lateinit var appInstance : TAApplication
    }
    lateinit var preferences : TAPreferences

    override fun onCreate() {
        super.onCreate()
        initGlobalInstance()
    }

    /**
     * The method called to init global instance
     */
    private fun initGlobalInstance() {
        appInstance = this
        preferences = TAPreferences(this)
    }
}