package com.example.quiztech

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quiztech.auth.LoginActivity
import com.example.quiztechimport.HomeActivity
import com.prvt.sreezzyuser.common.Utils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // setContentView(R.layout.activity_main) // We'll navigate away, so no need to set content for main
        // ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
        //     val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        //     v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        //     insets
        // }

       val isRegistered= Utils.getData(this@MainActivity, Utils.IS_REGISTERED,"0")

        // Navigate to LoginActivity
        if(isRegistered=="1")
        startActivity(Intent(this, HomeActivity::class.java))
        else
        startActivity(Intent(this, LoginActivity::class.java))
        // Finish MainActivity so the user cannot navigate back to it with the back button
        finish()
    }
}