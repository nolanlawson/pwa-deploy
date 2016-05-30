'use strict';

// use the global adb if you find it, else install android sdk and use that

var denodeify = require('denodeify');
var which = denodeify(require('which'));
var fs = require('fs');

var promise = Promise.resolve().then(function () {
  return which('adb').then(function (absoluteAdb) {
    console.log('Using global adb: ' + absoluteAdb);
    return absoluteAdb;
  });
}).catch(function () {
  // couldn't find a global `adb`
  throw new Error('couldn\'t find `adb`. Please install the Android SDK, e.g. using:\n' +
    '  brew install android\n' +
    'and then ensure that `adb` is available on the global path.');
});

function adb () {
  return promise;
}

module.exports = adb;
