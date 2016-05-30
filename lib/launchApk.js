'use strict';

var path = require('path');
var denodeify = require('denodeify');
var fs = require('fs');
var stat = denodeify(fs.stat);
var adb = require('./adb');
var childProcess = require('child-process-promise');
var Promise = require('bluebird');

function execAdb (specifiedDevice, commands) {
  return adb().then(function (adbPath) {
    var promise = Promise.resolve();
    commands.forEach(function (cmd) {
      promise = promise.then(function () {
        var fullCmd = (specifiedDevice ? ('-s ' + specifiedDevice + ' ') : '') + cmd;
        console.log('> adb ' + fullCmd);
        return childProcess.exec(adbPath + ' ' + fullCmd);
      }).then(function (child) {
        console.log(child.stdout);
      });
    });
    return promise;
  });
}

function launchApk (dir, specifiedDevice, port) {
  return stat(dir).then(function (stats) {
    if (!stats.isDirectory()) {
      throw new Error(dir + ' is not a directory. ' +
        'Please enter a valid directory path.');
    }
    return execAdb(specifiedDevice, [
      'uninstall com.nolanlawson.pwadeploy',
      'install -r ' + path.resolve(__dirname, '../android/app/build/outputs/apk/app-debug.apk'),
      'shell rm -fr /sdcard/pwa-deploy/www',
      'shell mkdir -p /sdcard/pwa-deploy',
      'push ' + dir + ' /sdcard/pwa-deploy/www',
      'shell "run-as com.nolanlawson.pwadeploy cp -r /sdcard/pwa-deploy/www /data/data/com.nolanlawson.pwadeploy/files"',
      'shell rm -fr /sdcard/pwa-deploy/www',
      'shell am start -n com.nolanlawson.pwadeploy/.MainActivity -e port ' + port
    ]);
  });
}

module.exports = launchApk;
