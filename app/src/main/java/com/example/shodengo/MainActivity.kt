package com.example.shodengo

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.shodengo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val webView = binding.webView

        // WebViewClient allows you to handle onPageFinished and override URL loading.
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                // Add a delay before injecting additional CSS and JavaScript
                Handler().postDelayed({
                    // Inject additional CSS to the existing .navbar class
                    val customCSS = ".navbar { flex-direction: column !important; gap: 1em !important; }"
                    val jsInjection = "(function() { " +
                            "var style = document.createElement('style'); " +
                            "style.innerHTML = '$customCSS'; " +
                            "document.head.appendChild(style); " +
                            "console.log('Additional CSS injected into .navbar'); " +
                            "document.styleSheets[0].insertRule('img { max-width: 100% !important; }', 0); " +
                            "console.log('Max-width rule injected for images'); " +
                            "})();"

                    // Load the JavaScript code to inject additional CSS and modify images into the WebView
                    webView.evaluateJavascript(jsInjection, null)
                }, 100) // Adjust the delay as needed
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                // Log or handle the error
            }
        }

        // Enable JavaScript settings
        webView.settings.javaScriptEnabled = true
        // Enable zoom feature
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false

        // Load the URL of the website
        webView.loadUrl("https://www.shodan.io/")
    }

    // Handle back button press
    override fun onBackPressed() {
        // If WebView can go back, go back
        if (binding.webView.canGoBack())
            binding.webView.goBack()
        // If WebView cannot go back, exit the application
        else
            super.onBackPressed()
    }
}
