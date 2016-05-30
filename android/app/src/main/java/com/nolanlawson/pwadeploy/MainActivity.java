package com.nolanlawson.pwadeploy;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener {

  private static final UtilLogger LOG = new UtilLogger(MainActivity.class);
  private HttpServer httpServer;

  private TextView textView;
  private Button openBrowserButton;
  private Button copyUrlButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LOG.d("onCreate()");
    setContentView(R.layout.activity_main);

    int port = getPort();

    httpServer = new HttpServer(this, port);
    try {
      httpServer.start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    textView = ((TextView)findViewById(android.R.id.text1));
    openBrowserButton = ((Button)findViewById(android.R.id.button1));
    copyUrlButton = ((Button)findViewById(android.R.id.button2));

    textView.setText("Your app is now running at:\n" + getUri());
    openBrowserButton.setOnClickListener(this);
    copyUrlButton.setOnClickListener(this);

    launchBrowser();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (httpServer != null) {
      httpServer.stop();
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case android.R.id.button1:
        launchBrowser();
        break;
      case android.R.id.button2:
        copyUrl();
        break;
    }
  }

  private void launchBrowser() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUri()));
    startActivity(intent);
  }

  private void copyUrl() {
    ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = ClipData.newUri(getContentResolver(),"URI", Uri.parse(getUri()));
    clipboard.setPrimaryClip(clip);
    Toast.makeText(this, "URL copied to clipboard", Toast.LENGTH_SHORT).show();
  }

  private String getUri() {
    return "http://127.0.0.1:" + getPort();
  }

  private int getPort() {
    return getIntent() != null && getIntent().hasExtra("port") ?
        Integer.parseInt(getIntent().getStringExtra("port")) : 3000;
  }
}
