package com.nolanlawson.pwadeploy;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

  private static final UtilLogger LOG = new UtilLogger(MainActivity.class);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LOG.d("onCreate()");
    setContentView(R.layout.activity_main);
    ServiceHelper.startBackgroundServiceIfNotAlreadyRunning(this);
  }
}
