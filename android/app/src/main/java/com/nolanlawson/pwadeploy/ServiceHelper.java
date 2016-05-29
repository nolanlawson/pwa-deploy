package com.nolanlawson.pwadeploy;

import java.util.List;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class ServiceHelper {

  private static final UtilLogger LOG = new UtilLogger(ServiceHelper.class);

  public static synchronized void startBackgroundServiceIfNotAlreadyRunning(
      Context context) {
    LOG.d("startBackgroundServiceIfNotAlreadyRunning()");
    boolean alreadyRunning = ServiceHelper.checkIfServiceIsRunning(context, PWADeployService.class);

    if (alreadyRunning) {
      LOG.d("already running");
    } else {
      LOG.d("not already running, starting");
      Intent intent = new Intent(context, PWADeployService.class);
      context.startService(intent);
    }
  }

  public static boolean checkIfServiceIsRunning(Context context, Class<?> service) {

    String serviceName = service.getName();
    ComponentName componentName = new ComponentName(context.getPackageName(), serviceName);
    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningServiceInfo> procList = activityManager.getRunningServices(Integer.MAX_VALUE);
    if (procList != null) {
      for (ActivityManager.RunningServiceInfo appProcInfo : procList) {
        if (appProcInfo != null && componentName.equals(appProcInfo.service)) {
          LOG.d("%s is already running", serviceName);
          return true;
        }
      }
    }
    LOG.d("%s is not running", serviceName);
    return false;
  }
}
