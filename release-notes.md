# Speed Meter v3.0.0

🎉 **Speed Meter v3.0.0 updates the build tools, tidies the settings screen, and makes the notification appear immediately when monitoring starts.**

---

## What's New

### ⚡ Faster notification start
- The status bar notification now appears immediately when monitoring starts, without waiting for the system to schedule it.

### 🧭 Cleaner settings screen
- The indicator switch is now a card at the top of the settings page.
- Settings sections have a clearer layout, and section titles line up with your phone's screen corners.
- The page scrolls cleanly above the navigation bar, and a matching bar fills the bottom edge.

### 🎨 Simpler typography
- The app uses Material 3's standard text styles, so text matches the rest of your system.

### 🔧 Under the hood
- Updated to Android Gradle Plugin 9.3.1, Kotlin 2.3.21, and Gradle 9.6.1.
- Kotlin support now comes built into the Android Gradle Plugin.
- Release builds keep debug symbols for two native libraries, which helps with crash reports.
- Version bumped to **3.0.0** (version code 14).

---

## Changes to Know About
- Settings section titles, the indicator switch's location, and the bottom bar may look different from v2.0.0.
- The navigation bar is no longer set to transparent. It is filled with the app's surface color instead.

---

## Compatibility
- Requires **Android 8.0 (Oreo)** or newer.
- Device-matched corners need **Android 12** or newer. Older versions use the standard corner.

---

## Known Notes
- Daily totals are most accurate with **Usage access** granted. Without it, they are estimates.
- Some phone makers add their own background limits. You may need to allow Speed Meter to run in the background in your phone's battery settings.
- This release uses a pre-release (alpha) version of Material 3, so minor visual quirks may appear.

Thanks for using **Speed Meter**! 🚀
