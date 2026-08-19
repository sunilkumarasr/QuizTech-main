package com.example.quiztech

import android.app.Application
import com.prvt.sreezzyuser.common.Utils

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
          Utils.access_token = Utils.getData(this,"access_token","").toString()

    }
}