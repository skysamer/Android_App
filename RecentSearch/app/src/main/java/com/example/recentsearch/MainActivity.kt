package com.example.recentsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fun loadData():MutableList<Memo>{
            val data:MutableList<Memo> = mutableListOf()
            for(no in 1..100){
                val title="최근 검색 장소 ${no}"
                val date=System.currentTimeMillis()

                var memo=Memo(title, date)
                data.add(memo)
            }
            return data;
        }

        val data:MutableList<Memo> =loadData()
        var adapter=CustomAdapter()
        adapter.listData=data
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(this)
    }
}