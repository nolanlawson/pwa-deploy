#!/usr/bin/env bash

if [ -z ~/.keystore_config ]; then
  echo "couldn't find keystore config!"
  echo "you need a ~.keystore_config file with:"
  echo "export ANDROID_KEYSTORE=keystore"
  echo "export ANDROID_STORE_PASSWORD=pass"
  echo "export ANDROID_ALIAS=name"
  echo "export ANDROID_PASSWORD=pass"
  exit 1
fi

source ~/.keystore_config

cd android
./gradlew assembleDebug
