package com.itsol.ironsourceutils

import android.app.Application
import com.itsol.ironsourcelib.AppOpenManager

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == TRIM_MEMORY_UI_HIDDEN){
            AppOpenManager.getInstance().timeToBackground = System.currentTimeMillis()
        }
    }
}