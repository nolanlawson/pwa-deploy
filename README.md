pwa-deploy
====

**Update:** don't use this. Use [Chrome port forwarding](https://developers.google.com/web/tools/chrome-devtools/debug/remote-debugging/local-server) instead.

Deploy a Progressive Web App (or any static website) to a connected Android device or emulator.
Designed for quick and easy debugging of PWAs.

This allows you to test Service Worker, since the site will be running on `localhost`.

Install
---

```bash
npm install -g pwa-deploy
```

Usage
---

```bash
pwa-deploy /path/to/www/directory
```

This will deploy the directory to the first available Android
device or emulator, and then open the site in your default mobile browser. Android 4.0+ is supported.

Advanced usage
----

```
Options:
  -d, --devices          list available devices
  -s, --specificDevice   specific device id. do `pwa-deploy devices` to see
                         available
  -p                     port to run on (default: 3000)
  -h, --help             Show help                                     [boolean]

Examples:
  pwa-deploy /path/to/www/                Load webapp on first available device
  pwa-deploy /path/to/www/ -s <deviceid>  Load webapp on specified device
  pwa-deploy devices                      List all available devices/emulators
```

Troubleshooting Android device issues
----

This tool doesn't require the [Android SDK](https://developer.android.com/studio/index.html),
but it will look for a global `adb` if you have it. Else it will use a prebuilt `adb`.

First off, be sure your device is available for USB debugging:

1. Go into **Settings** > **About phone/tablet** > Tap **Build number** several times until it unlocks developer mode
2. Then **Settings** > **Developer options** > **USB debugging** and enable access when the prompt shows

If you have the Android SDK installed, try checking for attached devices:

```bash
adb devices
```

You should see a list of devices:

```
List of devices attached
010a2f182ccaa952	device
```

If `adb` isn't working, then you can sometimes fix it by doing `adb kill-server && adb start-server`.
