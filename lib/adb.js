'use strict';

// use the global adb if you find it, else install android sdk and use that

var denodeify = require('denodeify');
var which = denodeify(require('which'));
var path = require('path');
var os = require('os');

var promise = Promise.resolve().then(function () {
  return which('adb').then(function (absoluteAdb) {
    console.log('Using global adb: ' + absoluteAdb);
    return absoluteAdb;
  });
}).catch(function () {
  // couldn't find a global `adb`, fall back to local
  console.log('Using local adb');
  if (os.platform() === 'darwin') {
    return path.resolve(__dirname, '../exec/darwin/adb');
  } else if (os.platform() === 'win32') {
    return path.resolve(__dirname, '../exec/win32/adb.exe');
  } else if (os.platform() === 'linux') {
    return path.resolve(__dirname, '../exec/linux/adb');
  }
  throw new Error('unsupported platform');
}).catch(function (err) {
  console.error(err);
  throw new Error('couldn\'t find `adb`. Please install the Android SDK ' +
    'and then ensure that `adb` is available on the global path.');
});

function adb () {
  return promise;
}

module.exports = adb;
