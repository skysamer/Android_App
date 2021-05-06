package com.example.containerrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data:MutableList<Memo> = loadData()
        var adapter=CustomAdapter()
        adapter.listData=data
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(this)
    }

    fun loadData():MutableList<Memo>{
        val data:MutableList<Memo> = mutableListOf()

        for(no in 0..100){
            val title="이것이 코틀린 안드로이드다 ${no+1}"
            val date=System.currentTimeMillis()
            var memo=Memo(no, title, date)
            data.add(memo)
        }
        return data
    }
}