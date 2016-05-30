'use strict';

var path = require('path');
var denodeify = require('denodeify');
var fs = require('fs');
var stat = denodeify(fs.stat);
var adb = require('./adb');
var zipfile = require('zipfile');
var Promise = require('bluebird');
var temp = require('temp');
var tempdir = denodeify(temp.mkdir);
var unzip = require('unzip');

function updateApk (dir) {
  var sourceApk = path.resolve(__dirname,
    '../android/app/build/outputs/apk/app-release.apk');
  return tempdir('apk').then(function (explodedDir) {
    return new Promise(function (resolve, reject) {
      fs.createReadStream(sourceApk)
        .pipe(unzip.Extract({path: explodedDir}))
        .on('finish', resolve)
        .on('error', reject);
    }).then(function () {
      console.log(explodedDir);
    });
  });
}

function launchApk (dir) {
  return stat(dir).then(function (stats) {
    if (!stats.isDirectory()) {
      throw new Error(dir + ' is not a directory. ' +
        'Please enter a valid directory path.');
    }
    return updateApk(dir); /*.then(function (adbPath) {

      }).then(function () {
        return childProcess.exec(adbPath + ' install -r ' +
          path.resolve(__dirname, '../android/app/build/outputs/apk/app-release.apk'));
      }).then(function () {
        return childProcess.exec(adbPath +
          ' shell am start -n com.nolanlawson.pwadeploy/.MainActivity');
      });
    });*/
  });
}

module.exports = launchApk;