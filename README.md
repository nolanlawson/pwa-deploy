pwa-deploy
====

Deploy a Progressive Web App to a connected Android device or emulator. Designed for quick and easy debugging of PWAs.

This allows you to test Service Worker, since the site will be running on `localhost`.

Install
---

```bash
npm install pwa-deploy
```

This tool requires the [Android SDK](https://developer.android.com/studio/index.html).
See [Setting up the Android SDK](#setting-up-the-android-sdk) for instructions.

Usage
---

```bash
pwa-deploy <path> [options]
```

E.g.

```bash
pwa-deploy /path/to/www
```

This will deploy the directory provided to the first available attached Android
device and run it in your default browser.

You can do `pwa-deploy --help` for full options.

Setting up the Android SDK
----

This requires that you have the [Android SDK](https://developer.android.com/studio/index.html) installed. (You don't need all of Anrdoid studio installed.)

On Mac OS X, this is as easy as:

```bash
brew install android-sdk
```

For other platforms, use the link above.

Once installed, you should ensure that `adb` is available from the command line.
If not, you'll need to add it to your `PATH`; it's in the SDK under `platform-tools`.

Make sure you can do:

```bash
adb devices
```

You should see a list of devices:

```
List of devices attached
010a2f182ccaa952	device
```

If you don't, then you may need to go into your Android device's settings and enable ADB debugging. Usually this is:

1. Go into **Settings** > **About phone/tablet** > Tap **Build number** several times until it unlocks developer mode
2. Then **Settings** > **Developer options** > **USB debugging** and enable access when the prompt shows

If `adb` isn't working, then you can sometimes fix it by doing `adb kill-server && adb start-server`.

Advanced usage
----

```
Options:
  -d, --devices          list available devices
  -s, --specifiedDevice  specific device id. do `pwa-deploy devices` to see
                         available
  -p                     port to run on (default: 3000)
  -h, --help             Show help                                     [boolean]

Examples:
  pwa-deploy /path/to/www/                Load webapp on first available device
  pwa-deploy /path/to/www/ -s <deviceid>  Load webapp on specified device
  pwa-deploy devices                      List all available devices/emulators
```
