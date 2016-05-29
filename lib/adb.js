'use strict';

// use the global adb if you find it, else install android sdk and use that

var denodeify = require('denodeify');
var which = denodeify(require('which'));
var unzip = denodeify(require('unzip'));
var http = require('http');
var fs = require('fs');
var localdir = require('./localdir');
var path = require('path');
var request = require('request');

function getAndroidSdkLocation() {
  if ( /^win/.test(process.platform)) {
    return 'https://dl.google.com/android/android-sdk_r24.4.1-windows.zip';
  } else if (process.platform === 'darwin') {
    return 'https://dl.google.com/android/android-sdk_r24.4.1-macosx.zip';
  } else { // linux
    return  'https://dl.google.com/android/android-sdk_r24.4.1-linux.tgz';
  }
}

var promise = Promise.resolve().then(function () {
  return which('adb').then(function (absoluteAdb) {
    console.log('Using global adb: ' + absoluteAdb);
    return absoluteAdb;
  });
}).catch(function () {
  // couldn't find a global `adb`
  var location = getAndroidSdkLocation();
  console.log('Didn\'t find global `adb`, installing Android SDK...');
  return localdir().then(function (localdirPath) {
    var zipFile = path.resolve(localdirPath, 'sdk.zip');
    var sdkDir = path.resolve(localdirPath, "sdk");
    return new Promise(function (resolve, reject) {
      request.get(location)
        .on('error', reject)
        .pipe(zipFile)
        .on('error', reject)
        .on('finish', resolve);
    }).then(function () {
      return new Promise(function (resolve, reject) {
        fs.createReadStream(zipfile)
          .pipe(unzip.Extract({path: sdkDir}))
          .on('error', reject)
          .on('finish', resolve);
      });
    }).then(function () {
      return path.resolve(sdkDir, 'sdk', 'platform-tools', 'adb');
    });
  });
});

function adb() {
  return promise;
}

module.exports = adb;