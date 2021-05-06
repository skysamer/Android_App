package com.example.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.ListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setFragment()
    }

    fun goDetail(){
        val detailFragment=DetailFragment()

        val transaction=supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentLayout, detailFragment)
        transaction.addToBackStack("detail")
        transaction.commit()
    }

    fun goBack(){
        onBackPressed()
    }

    fun setFragment(){
        val listFragment:BlankFragment= BlankFragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.commit()
    }
}