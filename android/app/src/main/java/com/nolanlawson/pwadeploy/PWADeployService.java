package com.nolanlawson.pwadeploy;

import android.content.Intent;
import android.app.IntentService;

public class PWADeployService extends IntentService {

  private static final UtilLogger LOG = new UtilLogger(PWADeployService.class);

  public PWADeployService() {
    super(PWADeployService.class.getSimpleName());
  }

  @Override
  public void onCreate() {
    super.onCreate();
    LOG.d("onCreate()");
  }

  protected void onHandleIntent(Intent intent) {
    LOG.d("onHandleIntent()");
  }

}