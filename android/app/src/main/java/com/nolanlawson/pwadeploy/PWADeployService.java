package com.nolanlawson.pwadeploy;

import android.app.IntentService;
import android.content.Intent;

import java.io.IOException;

public class PWADeployService extends IntentService {

  private static final UtilLogger LOG = new UtilLogger(PWADeployService.class);

  private HttpServer httpServer;

  public PWADeployService() {
    super(PWADeployService.class.getSimpleName());
  }

  @Override
  public void onCreate() {
    super.onCreate();
    LOG.d("onCreate()");

    httpServer = new HttpServer(this);
    try {
      httpServer.start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    httpServer.stop();
  }

  protected void onHandleIntent(Intent intent) {
    LOG.d("onHandleIntent()");
  }

}