package com.vishishta.newsapp


import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request

import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest as JsonObjectRequest


class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView= findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter
    }

    private fun fetchData() {
       val url = "https://saurav.tech/NewsAPI/everything/fox-news.json"
       val jsonObjectRequest = JsonObjectRequest(
           Request.Method.GET, url, null,
           {
               val newsJsonArray = it.getJSONArray("articles")
               val newsArray = ArrayList<News>()
               for(i in 0 until newsJsonArray.length()){
                   val newsJsonObject = newsJsonArray.getJSONObject(i)
                   val news = News(
                       newsJsonObject.getString("title"),
                       newsJsonObject.getString("author"),
                       newsJsonObject.getString("url"),
                       newsJsonObject.getString("urlToImage")
                   )
                   newsArray.add(news)
               }
               mAdapter.updateNews(newsArray)
           },
           {

           }
       )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }

}