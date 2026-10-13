# Speed Meter

<p align="center">
  <img src="assets/ic_launcher.svg" width="120" height="120" alt="Speed Meter Icon"/>
</p>

Speed Meter is an Android app that shows your phone's current network speed, download and upload, in your status bar. It also shows how much mobile data and Wi-Fi data you have used today.

---

## What Can You Do With This App?

### ✅ Available Right Now

**Live network speed**
- **Status bar indicator.** A small icon in your status bar shows your total network speed, updated every second.
- **Download and upload.** The notification shows your download and upload speeds.
- **Speed units.** Choose **bytes per second** (KB/s, MB/s, GB/s) or **bits per second** (Kb/s, Mb/s, Gb/s).
- **Runs in the background.** The indicator keeps working while you use other apps.
- **Quiet notification.** It makes no sound and does not vibrate. It stays on your device only.

**Daily data usage**
- **Today's mobile and Wi-Fi data.** The notification shows how much **Data** (mobile) and **WiFi** you have used since midnight.
- **Accurate totals with Usage access.** Grant Usage access to get exact daily totals from Android. The app shows whether access is granted in **More → Notification layout**.
- **Without Usage access.** The app still shows totals, but they may be lower after a phone restart.
- **Data units.** Choose how sizes are shown:
  - Bits, decimal (Mb = 1000 Kb)
  - Bytes, decimal (MB = 1000 kB)
  - Bytes, binary (MiB = 1024 KiB), the default

**Home section**
- **Enable indicator.** Tap the row or its switch to turn the status bar indicator on or off. The indicator is **off** until you turn it on. Your choice is saved and respected every time you open the app.

**More section**
- **Notification settings.** Opens your phone's notification settings for Speed Meter.
- **Speed units.** Pick bytes or bits per second.
- **Data usage units.** Pick one of the three data unit styles.
- **Usage access.** Shows whether Usage access is granted, and opens the setting if it isn't.
- **Choose theme.** Pick **System default**, **Light mode** or **Dark mode**. Your choice is saved.
- **Help & feedback.** Choose **Suggest a feature** or **Report a bug**. Either option opens your email app with a message ready to send to the developer. Bug reports include your app version, phone model and Android version.
- **About.** Shows the app name, version and a short description.

**Look and feel**
- **Title bar.** The top of the screen shows "Speed Meter" and stays in place as you scroll.
- **Single scrolling page.** Home and More are on one page. Scroll down to reach More.
- **Custom switch.** The switch can be tapped or dragged.
- **Light, dark or system theme.** Follow your phone's setting, or choose light or dark mode in **More → Choose theme**.
- **Wallpaper colours (Android 12 and newer).** On supported phones, the app picks its colours from your wallpaper.
- **Standard colours on older phones.** Phones without wallpaper colours get a built-in colour scheme.
- **Full-screen display.** Content runs edge to edge behind the status bar and navigation bar.
- **Matching system bars.** The status bar and navigation bar icons switch between light and dark to suit the theme.
- **Rotation and resizing.** The layout adjusts when you rotate your device or resize the window.
- **Keyboard-friendly.** The layout adjusts when the on-screen keyboard appears.
- **Speedometer app icon.** The icon shows a coloured gauge with a needle.
- **Themed icon (Android 13 and newer).** A single-colour icon matches your launcher's theme.
- **Smoother launch.** The app opens without a blank preview screen.

**Permissions**
- **Notifications (Android 13 and newer).** The app asks for permission to show notifications when you open it. The indicator needs this permission. If you deny it, the indicator will not appear until you allow it in your phone's settings.
- **Usage access (optional).** Lets the app read Android's data usage records for exact daily totals. You grant it in system settings. The app does not send your data anywhere.
- **Internet and network status.** Used to read your network speed and data use.

---

## Planned Features (Not Available Yet)

- **Speed history.** Review past speeds.
- **Speedometer gauge.** A live gauge on the main screen that shows your speed.
- **Usage history.** Look back at past days' data usage.

---

## How to Install

1. Download the APK file for your device from the project's Releases page.
2. On your Android phone, go to **Settings → Install Unknown Apps** and allow installation from your browser or file manager.
3. Open the downloaded APK and tap **Install**.
4. Open **Speed Meter**. If asked, allow notifications.
5. On the Home section, turn on **Enable indicator** to see your speed in the status bar.
6. Optional: in **More → Notification layout**, tap **Usage access** and allow it for exact daily totals.

---

## Requirements

- Android **8.0 (Oreo)** or higher
- Works on phones, tablets and foldables

---

## For Developers

- Kotlin with Jetpack Compose and Material 3
- Background service with live speed readings from TrafficStats and daily totals from NetworkStatsManager
- See `CHANGELOG.txt` for the full tech stack and feature history

---

## Support

Have a problem or an idea?
Use **More → Help & feedback** in the app, or reach out at **sdcgroupofficial@gmail.com**

---

## Made With ❤️ from India
