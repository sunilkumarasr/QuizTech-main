package com.example.quiztech

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

typealias ActivityInflater<T> = (LayoutInflater) -> T
 abstract class BaseActivity<VB : ViewBinding>(
    private val bindingInflater: ActivityInflater<VB>
) : AppCompatActivity() {
    lateinit var binding: VB
    private set // Optional: make the setter private if only BaseActivity should initialize it

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the binding
        binding = bindingInflater.invoke(layoutInflater)
        // Set the content view using the root of the binding
        setContentView(binding.root)

        // Common setup can go here, using binding if needed
        // e.g., setupToolbar("Default Title")
    }
    fun showToast(msg:String){
        
    }


}