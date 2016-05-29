'use strict';

var denodeify = require('denodeify');
var mkdirp = denodeify(require('mkdirp'));
var path = require('path');

var home = process.env.HOME || process.env.USERPROFILE;

var dir = path.resolve(home, '.pwa-deploy');

function localdir () {
  return mkdirp(dir).then(function () {
    return dir;
  });
}

module.exports = localdir;
