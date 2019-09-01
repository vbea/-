package com.binxin.zdapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.binxin.zdapp.R;
import com.binxin.zdapp.classes.MyThemes;
import android.webkit.*;

public class HtmlViewer extends Activity
{
	private LinearLayout titleLayout;
	private WebView web;
	private TextView webtitle;
	private String webFile;
	private ProgressBar webProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.htmlview);
		
		titleLayout = (LinearLayout) findViewById(R.id.themeLayout);
		web = (WebView) findViewById(R.id.web_viewr);
		webtitle = (TextView) findViewById(R.id.txt_htmlTitle);
		webProgress = (ProgressBar) findViewById(R.id.html_progress);
		ImageButton back = (ImageButton) findViewById(R.id.btn_close);
		WebSettings wset = web.getSettings();
		wset.setJavaScriptEnabled(true);
		wset.setSupportZoom(true);
		wset.setBuiltInZoomControls(true);
		wset.setDisplayZoomControls(false);
		//html5
		wset.setDomStorageEnabled(true);
		wset.setDatabaseEnabled(true);
		wset.setDatabasePath(getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath());
		wset.setAppCacheEnabled(true);
		wset.setAppCachePath(getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath());
		wset.setPluginState(WebSettings.PluginState.ON);
		wset.setUseWideViewPort(true);
		wset.setLoadWithOverviewMode(true);
		wset.setUserAgentString(wset.getUserAgentString());
		wset.setGeolocationEnabled(true);
		wset.setGeolocationDatabasePath(getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath());
		wset.setCacheMode(WebSettings.LOAD_DEFAULT);
		wset.setAppCacheMaxSize(1024*1024*10);
		wset.setAllowFileAccess(true);
		wset.setRenderPriority(WebSettings.RenderPriority.HIGH);
		wset.setJavaScriptCanOpenWindowsAutomatically(true);
		
		Intent intent = getIntent();
		if (intent != null && intent.getAction() != null)
		{
			if (intent.getAction().equals(Intent.ACTION_VIEW))
			{
				Uri uri = intent.getData();
				webFile = uri.getPath();
				web.loadUrl("file:///" + webFile);
			}
		}
		
		web.setWebChromeClient(new WebChromeClient()
		{
			@Override
			public void onReceivedTitle(WebView view, String title)
			{
				super.onReceivedTitle(view, title);
				webtitle.setText(title);
			}
			
			@Override
			public void onProgressChanged(WebView view, int newProgress)
			{
				webProgress.setProgress(newProgress);
				webProgress.setVisibility(newProgress == 100 ? View.INVISIBLE : View.VISIBLE);
			}
			
			public void onExceededDatabaseQuota(String url, String databasein, long quota, long estime, long totalq, WebStorage.QuotaUpdater update)
			{
				update.updateQuota(5 * 1024 * 1024);
			}
		});
		
		web.setWebViewClient(new WebViewClient()
		{
			@Override
			public void onPageFinished(WebView view, String url)
			{
				webtitle.setText(view.getTitle());
			}
		});
		
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
	}

	@Override
    //设置回退 
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法 
    public boolean onKeyDown(int keyCode, KeyEvent event)
	{ 
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack())
		{ 
            web.goBack(); //goBack()表示返回WebView的上一页面 
            return true; 
        } 
        return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		MyThemes.setThemes(this, titleLayout);
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	} 
}
