#!/usr/bin/env node

'use strict';

var yargs = require('yargs')
  .usage('Usage: $0 <path> [options]')
  .example('$0 /path/to/www/', 'Load webapp on first available device')
  .example('$0 /path/to/www/ -s <deviceid>', 'Load webapp on specified device')
  .example('$0 devices', 'List all available devices/emulators')
  .describe('d', 'list available devices')
  .alias('d', 'devices')
  .describe('s', 'specific device id. do `pwa-deploy devices` to see available')
  .alias('s', 'specificDevice')
  .describe('p', 'port to run on (default: 3000)')
  .alias('p, port')
  .help('h')
  .alias('h', 'help');

var Promise = require('bluebird');
if (!global.Promise) {
  global.Promise = Promise; // required for denodeify
}
var childProcess = require('child-process-promise');
var path = require('path');
var adb = require('./adb');
var arg = yargs.argv;
var launchApk = require('./launchApk');

if (arg.devices) {
  Promise.resolve().then(function () {
    return adb().then(function (adbPath) {
      return childProcess.exec(adbPath + ' devices');
    });
  }).then(function (res) {
    process.stdout.write(res.stdout);
    process.exit(0);
  }).catch(function (err) {
    console.error(err.stack);
    process.exit(1);
  });
} else if (arg._[0]) { // normal operation
  var dir = path.resolve(arg._[0]);
  var port = arg.port || 3000;
  launchApk(dir, arg.specificDevice, port).catch(function (err) {
    console.error(err.stack);
    process.exit(1);
  });
} else {
  yargs.showHelp();
  process.exit(0);
}
