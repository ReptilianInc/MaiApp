package com.mai.nix.maiapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebViewActivity extends AppCompatActivity {
    private static final String URI = "com.mai.nix.maiapp.uri";
    private WebView mWebView;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private Uri mUri;

    public static Intent newInstance(Context context, Uri uri){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(URI, uri);
        return intent;
    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mToolbar = (Toolbar) findViewById(R.id.web_toolbar);
        setSupportActionBar(mToolbar);
        mWebView = (WebView)findViewById(R.id.webview);
        mProgressBar = (ProgressBar)findViewById(R.id.webview_progress_bar);
        mProgressBar.setMax(100);
        mUri = getIntent().getParcelableExtra(URI);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView webView, int newProgress){
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
            public void onReceivedTitle(WebView webView, String title) {
                WebViewActivity.this.setTitle(webView.getTitle());
            }
        });

        mWebView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest resourceRequest){
                return false;
            }
        });
        mWebView.loadUrl(mUri.toString());
    }
}
