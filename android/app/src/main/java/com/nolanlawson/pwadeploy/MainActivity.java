package com.nolanlawson.pwadeploy;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;

public class MainActivity extends Activity {

  private static final UtilLogger LOG = new UtilLogger(MainActivity.class);
  private HttpServer httpServer;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LOG.d("onCreate()");
    setContentView(R.layout.activity_main);
    //ServiceHelper.startBackgroundServiceIfNotAlreadyRunning(this);
    httpServer = new HttpServer(this);
    try {
      httpServer.start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
//
//    WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
//    int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
//    final String formatedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
//        (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
//    LOG.d("Please access! http://" + formatedIpAddress + ":3000");

    if (Build.VERSION.SDK_INT >= 19) {
      WebView.setWebContentsDebuggingEnabled(true);
    }
    WebView webView = ((WebView)findViewById(R.id.web_view));
    webView.loadUrl("http://127.0.0.1:" + HttpServer.PORT);

    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
      }
    });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (httpServer != null) {
      httpServer.stop();
    }
  }
}
