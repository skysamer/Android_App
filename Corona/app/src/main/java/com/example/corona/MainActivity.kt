package com.example.corona

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.ListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setFragment()
    }

    fun setFragment(){
        val buttonFragment:ButtonFragment= ButtonFragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.add(R.id.map, buttonFragment)
        transaction.commit()
    }
}