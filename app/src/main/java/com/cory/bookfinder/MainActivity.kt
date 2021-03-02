package com.cory.bookfinder

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        val themeData = ThemeData(this)
        if(themeData.loadDarkModeState()) {
            setTheme(R.style.AMOLED)
        }
        else {
            setTheme(R.style.AppTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this)
        val adView = AdView(this)
        adView.adSize = AdSize.BANNER
        adView.adUnitId = "ca-app-pub-4546055219731501/1995733094"
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView.adListener = object : AdListener() {

        }

        firebaseAnalytics = Firebase.analytics

        textView4.visibility = View.INVISIBLE

        button.setOnClickListener {
            if (editText.text.toString() == "") {
                textView4.visibility = View.VISIBLE
                textView4.text = getString(R.string.cant_be_thinking)
            } else {
                val isOnlineFun = isOnline(this)
                if (isOnlineFun) {
                    val i = Intent(this, SearchResults::class.java)
                    i.putExtra("whats on your mind", editText.text.toString())
                    startActivity(i)
                }
                else {
                    Toast.makeText(this, "You have no connection, please try again", Toast.LENGTH_LONG).show()
                }
            }
        }
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
        constraintLayout.setOnClickListener {
            if (editText.hasFocus())
                hideKeyboard()
            editText.clearFocus()
        }
        button2.setOnClickListener {
            editText?.text?.clear()
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if(capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            }
            else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toggle_dark_mode -> {
                val themeData = ThemeData(this)
                if(!themeData.loadDarkModeState()) {
                    themeData.setDarkModeState(true)
                    restartApplication()
                }
                else {
                    themeData.setDarkModeState(false)
                    restartApplication()
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun restartApplication() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
        this.finishAffinity()
    }
    private fun hideKeyboard() {
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.SHOW_FORCED)
        }
        if (editText.hasFocus()) {
            editText.clearFocus()
        }
    }
}
