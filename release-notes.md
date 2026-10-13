# Speed Meter v0.3.0: Live Network Speed Indicator

**Speed Meter v0.3.0** is the first release that measures your network speed. It shows your live download and upload speed in your status bar and adds a switch to turn it on or off.

> **Known issue:** If you turn the indicator off and then open the app, the indicator starts again. A fix is coming in an upcoming release.

---

## What's New

### 📶 Live Network Speed Indicator
- **Speed in your status bar.** A small icon shows your total network speed (download plus upload), updated every second.
- **Download and upload.** The notification shows both speeds.
- **Smart units.** Speeds show in **KB/s**, and switch to **MB/s** once they pass 1,024 KB/s.
- **Runs in the background.** The indicator runs as a foreground service, so it stays visible while you use other apps.
- **Quiet by design.** The notification is low-importance, with no sound, no vibration, and no badge. It stays on your device only.

### ⚙️ Indicator Switch
- **"Enable indicator" switch** on the main screen turns the status bar indicator on or off.
- **Remembers your choice.** The setting is saved on your device.
- **Haptic feedback and screen reader support.** The switch vibrates when toggled and is labelled "Toggle NetSpeed status bar indicator" for accessibility.

### 🧭 Floating Bottom Toolbar
- A new **floating toolbar** with **Home** and **More** buttons sits at the bottom of the screen. The buttons do nothing yet.
- **Hides while scrolling.** It slides out of the way as you scroll down and returns when you scroll up.
- **Adapts to small screens.** Text labels hide on very narrow screens.

### 🔔 Notification Permission (Android 13 and newer)
- The app asks for **notification permission** when you open it, so the indicator can appear.
- If you deny it, the indicator will not appear until you allow notifications in your phone's settings.
- Earlier Android versions do not need this prompt.

---

## Under the Hood
- Material 3 updated to **1.5.0-alpha03** (pre-release) for the floating toolbar.
- Added **DataStore Preferences** for saving settings, plus **Lifecycle ViewModel and Runtime for Compose**, and **Kotlin Coroutines**.
- The app declares these permissions:
  - Foreground service (data sync)
  - Notifications
  - Network state
  - Internet
- The switch component is now reusable, with a configurable accessibility label.

---

## Compatibility
- Requires **Android 8.0 (Oreo) or newer**.
- Release builds are signed with **v1, v2, v3, and v4** signature schemes and verified before publishing.

---

## Known Notes
- This release uses a pre-release (alpha) version of Material 3, so minor visual quirks may appear.
- The theme toggle in the top bar is still in progress and will be added in an upcoming release.
- The Home and More buttons are placeholders for now.

Thanks for using **Speed Meter**! 🚀
