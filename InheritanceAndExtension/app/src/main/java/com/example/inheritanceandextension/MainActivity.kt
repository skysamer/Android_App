package com.example.inheritanceandextension

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var parent=Parent()
        parent.sayHello()

        var child=Child()
        child.myHello()

        testStringExtension()
    }

    fun testStringExtension(){
        var original="Hello"
        var added=" Guys"
        Log.d("Extension", "added를 더한 값은 ${original.plus(added)}")
    }
}

open class Parent{
    var hello:String="안녕하세요"
    fun sayHello(){
        Log.d("Extension", "${hello}")
    }
}

class Child:Parent(){
    fun myHello(){
        hello="Hello"
        sayHello()
    }
}

open class BaseClass{
    open fun opened(){

    }
    fun notOpened(){

    }
}

class childClass:BaseClass(){
    override fun opened() {
    }
}

open class BaseClass2{
    open var opened:String="I am"
}
class ChildClass2:BaseClass2(){
    override var opened:String="You are"
}
fun String.plus(word:String):String{
    return this+word
}