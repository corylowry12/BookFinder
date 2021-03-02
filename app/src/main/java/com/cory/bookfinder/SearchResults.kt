package com.cory.bookfinder

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import java.lang.NullPointerException

class SearchResults : AppCompatActivity() {

    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

            MobileAds.initialize(this)
            val adView = AdView(this)
            adView.adSize = AdSize.BANNER
            adView.adUnitId = "ca-app-pub-4546055219731501/1245200733"
            val mAdView = findViewById<AdView>(R.id.adView)
            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)
            mAdView.adListener = object : AdListener() {

            }
        try {
            webView = findViewById(R.id.webView)
            webView!!.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                    view?.loadUrl(url)
                    return true
                }
            }

            val `in` = intent
            val tv1 = `in`.extras!!.getString("whats on your mind")

            webView!!.loadUrl("https://www.google.com/search?q=books+about+$tv1")
        }
        catch (e: NullPointerException) {
            Toast.makeText(this, "There was an error", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}


