package com.example.aclass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class Kotlin(){
    init {
        Log.d("class", "Kotlin 클래스 생성")
    }
}

class KotlinTwo{
    constructor(value: String){
        Log.d("class", "kotlinTwo: ${value}")
    }
}

class KotlinThree{
    var one: String="none"

    fun printOne(){
        Log.d("class", "one: ${one}")
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Kotlin()

        KotlinTwo("hello")

        var kotlin=KotlinThree()
        kotlin.one="hihihi"
        kotlin.printOne()

    }
}