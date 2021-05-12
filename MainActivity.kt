package com.example.upnews

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.view.menu.MenuAdapter
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.upnews.News as News

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var madapter:NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView=findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager=LinearLayoutManager(this)
        fetchData()
        madapter= NewsListAdapter(this)
        recyclerView.adapter=madapter

    }

    private fun fetchData() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json"

// Request a string response from the provided URL.
        val jasonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener{

                    val newsJasonArray = it.getJSONArray("articles")
                    val newsArray = ArrayList<News>()

                    for(i in 0 until newsJasonArray.length()){

                        val newsJsonObject=newsJasonArray.getJSONObject(i)
                        val news = News (
                            newsJsonObject.getString("title") ,
                            newsJsonObject.getString("author") ,
                            newsJsonObject.getString("url") ,
                            newsJsonObject.getString("urlToImage")
                        )
                        newsArray.add(news)
                    }
                    madapter.updateNews(newsArray)

                },
                {

                },
        )

// Add the request to the RequestQueue.
        queue.add(jasonObjectRequest)


    }

    override fun onItemClicked(item: News) {

        val builder= CustomTabsIntent.Builder();
        val customTabsIntent  = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));

    }
}