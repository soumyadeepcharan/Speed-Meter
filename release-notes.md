# Speed Meter v1.0.0

🎉 **Speed Meter v1.0.0 is the first stable release.** Notification text now fits your screen, the app starts reliably after a restart, and the Quit button and dialog buttons have a new look.

---

## What's New

### 🔔 Adaptive notification text
- The Down, Up, Data, and WiFi values shrink their text automatically (from 13sp down to 7sp) when a value is too long.
- Long values stay readable instead of being cut off.
- Tighter spacing and no extra font padding use the available width better.

### 🚀 More reliable start on boot
- With **Start on boot** on, the indicator now starts after every restart.
- This works even if you quit the app or turned the indicator off before shutting down.

### ⏻ Clearer Kill Service button
- The top bar button is now a labeled **Kill Service** button.
- It uses the app's error color and a power-off icon, so its purpose is obvious.

### 🎨 Refreshed buttons and Help sheet
- Dialog confirm buttons (such as **Close**) are now filled buttons.
- The Help & feedback sheet has a drag handle again.
- The sheet's height accounts for the status bar, so it fits better on every screen.
- Help options have larger rounded corners.

### 🔒 Rotation follows your settings
- The app follows your phone's rotation lock instead of always rotating with the sensor.
- If rotation is locked, the app stays in its current orientation.

---

## Fixes & Improvements
- Removed haptic feedback from the toggle switch for a calmer feel.
- Simplified dialog code so dialogs behave consistently.

---

## Under the Hood
- Settings code is split into focused files: `Indicator.kt`, `NotificationSettings.kt`, `NotificationLayout.kt`, `General.kt`, and `Support.kt`.
- `HomeScreen.kt` was removed and its contents moved into those files.
- The Help & feedback sheet moved into the shared widgets file.
- Version bumped to **1.0.0** (version code 12).

---

## Compatibility
- Requires **Android 8.0 (Oreo)** or newer.

---

## Known Notes
- Daily totals are most accurate with **Usage access** granted. Without it, they are estimates.
- Some phone makers add their own background limits. You may need to allow Speed Meter to run in the background in your phone's battery settings.
- This release uses a pre-release (alpha) version of Material 3, so minor visual quirks may appear.

Thanks for using **Speed Meter**! 🚀
