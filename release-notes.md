# Speed Meter v2.0.0

🎉 **Speed Meter v2.0.0 redesigns the status bar notification with icons, uses the full screen edge to edge, and matches corners to your phone's screen.**

---

## What's New

### 🔔 Icon-led notification
- Download, upload, data, and WiFi each have their own icon, so you can read the notification at a glance.
- The four values sit in equal-width columns on one line.
- Notification text is now dark on light themes and light on dark themes.

### 🖼️ Edge-to-edge screens
- The app draws behind the status bar and navigation bar.
- The Settings screen keeps its bottom content clear of the navigation bar.
- Status and navigation bar icons follow your light or dark theme.

### 📐 Corners that match your phone
- On Android 12 and newer, the app reads your screen's corner radius and uses it for buttons, dialogs, and sheets.
- Older devices and devices that don't report it use a standard 28dp corner.
- The Help & feedback sheet has square bottom corners so it sits neatly at the screen edge.

### ⏻ Compact Quit button
- The top bar button is now a compact red **Quit** button with a fixed width, so the top bar stays steady.
- The indicator's Notification settings button has been removed.

### 🔄 Notification stays current on rotation and theme changes
- The notification is rebuilt straight away when the screen configuration changes, so it never shows an outdated layout.

### 🎛️ Track and thumb colors
- The switch's track and thumb colors now change instantly when toggled.

---

## Changes to Know About
- Long values in the notification are now cut off with "…" at a fixed text size. The adaptive text sizing from v1.0.0 has been replaced.
- The top bar button now reads **Quit** instead of **Kill Service**. It does the same thing: it stops monitoring and removes the notification.

---

## Under the Hood
- Last notification state is cached in the monitoring service.
- Shape tokens are derived from the device's corner radius.
- Removed the unused settings icon drawable.
- Version bumped to **2.0.0** (version code 13).

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
