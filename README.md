# Speed Meter

<p align="center">
  <img src="assets/ic_launcher.svg" width="120" height="120" alt="Speed Meter Icon"/>
</p>

Speed Meter is an Android app that shows your phone's current network speed (download and upload) in your status bar, so you can see it at a glance while you use other apps.

---

## What Can You Do With This App?

### ✅ Available Right Now

**Live network speed**
- **Status bar indicator.** A small icon in your status bar shows your total network speed, updated every second.
- **Download and upload.** The notification shows your download and upload speeds separately.
- **Easy-to-read units.** Speeds show in **KB/s**, and switch to **MB/s** once they pass 1,024 KB/s.
- **Runs in the background.** The indicator keeps working while you use other apps.
- **Quiet notification.** It makes no sound and does not vibrate. It stays on your device and cannot be dismissed while the indicator is on.

**Settings**
- **Enable indicator switch.** Turn the status bar indicator on or off from the main screen.
- **Saved choice.** Your switch setting is saved on your phone.

**Screen and look**
- **Title bar.** The top of the screen shows "Speed Meter" and stays in place as you scroll.
- **Floating toolbar.** A toolbar at the bottom has **Home** and **More** buttons. The buttons are placeholders and do nothing yet. The toolbar slides away as you scroll down and returns when you scroll up. On very narrow screens it shows icons only.
- **Light and dark mode.** The app follows your phone's system setting.
- **Wallpaper colours (Android 12 and newer).** On supported phones, the app picks its colours from your wallpaper.
- **Full-screen display.** Content runs edge to edge behind the status bar and navigation bar.
- **Matching system bars.** The status bar and navigation bar icons switch between light and dark to suit the theme.
- **Rotation.** The screen rotates with your device.
- **Keyboard-friendly layout.** The layout adjusts when the on-screen keyboard appears.
- **Speedometer app icon.** The icon shows a coloured gauge with a needle.
- **Themed icon (Android 13 and newer).** A single-colour icon version matches your launcher's theme.
- **Phones and tablets.** The layout adapts to different screen sizes.

**Permissions**
- **Notifications (Android 13 and newer).** The app asks for permission to show notifications when you open it. The indicator needs this permission. If you deny it, the indicator will not appear until you allow it in your phone's settings.
- **Internet and network status.** Used to read your network speed. The app does not send your data anywhere.

---

## Known Issue

- **Indicator restarts when you open the app.** If you turn the indicator off and later open Speed Meter, the indicator starts again, even though the switch shows off. We are fixing this in an upcoming update.

---

## Planned Features (Not Available Yet)

- **Daily data usage.** Track mobile data and Wi-Fi usage for each day.
- **Speed history.** Review past speeds.
- **Speedometer gauge.** A live gauge on the main screen that shows your speed.
- **Manual theme switch.** Choose light or dark mode from the title bar.
- **Settings.** Choose units (KB/s, MB/s, km/h, mph) and other options.
- **Working Home and More buttons.**
- **About screen.** App information and version details.

---

## How to Install

1. Download the APK file for your device from the project's Releases page.
2. On your Android phone, go to **Settings → Install Unknown Apps** and allow installation from your browser or file manager.
3. Open the downloaded APK and tap **Install**.
4. Open **Speed Meter**. If asked, allow notifications so the indicator can appear.

---

## Requirements

- Android **8.0 (Oreo)** or higher
- Works on phones and tablets

---

## For Developers

- Kotlin with Jetpack Compose and Material 3
- Background service with live speed readings from Android's TrafficStats
- See `CHANGELOG.txt` for the full tech stack and feature history

---

## Support

Have a problem or an idea?
Reach out at **sdcgroupofficial@gmail.com**

---

## Made With ❤️ from India
