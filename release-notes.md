# Speed Meter v0.8.1: Start on Boot, Quit, and Icon Speed

**Speed Meter v0.8.1** lets the indicator start after a restart, adds a quit button, lets you choose which speed the icon shows, and adds a shortcut for battery settings. The status bar icon is sharper, and the notification fields line up more evenly.

---

## What's New

### 🚀 Start on Boot
- A new **Start on boot** switch is in **More → General**.
- When it is on, the indicator starts automatically after your phone restarts.
- It starts only if the indicator is turned on, and only if you have not quit the app.

### ⏻ Quit Button
- A new power button in the top bar stops the indicator, removes the notification and closes the app.
- The indicator stays off after you quit, including after a restart. Turn it on again from the Home section.

### 🎚️ Choose the Icon Speed
- A new **Speed to show in indicator icon** option is in **More → Notification settings**.
- Pick **Total speed**, **Download speed** or **Upload speed** for the status bar icon.
- The change applies right away.

### 🔋 Battery Optimization Shortcut
- A new **Disable battery optimizations** option is in **More → General**.
- Its switch shows whether battery optimization still applies to Speed Meter.
- Tap it to open the system prompt that lets the indicator run in the background.

---

## Fixes and Improvements
- **Sharper status bar icon.** The icon is drawn at twice the resolution, with larger text.
- **Even notification layout.** The Down, Up, Data and WiFi fields share the width equally, so they fit more screen sizes.
- **Notification channel on launch.** The app creates the notification channel when it starts.
- **Quit is respected at launch.** After you quit, the app does not restart the indicator the next time you open it.
- **Smaller release builds.** Added shrinking rules, removed unused packaged files, and enabled split app bundles by language, screen density and processor type.

---

## Permissions
- **Start on boot** uses a permission that lets the app start after your phone restarts. It is used only when you turn this option on.
- **Battery optimization** lets the app ask Android to keep the indicator running in the background. The app asks only when you tap the option.
- **Usage access** (optional) gives exact daily totals. Turn it on in **More → Notification layout**.

---

## Compatibility
- Requires **Android 8.0 (Oreo) or newer**.
- Release builds are signed with **v1, v2, v3 and v4** signature schemes.

---

## Known Notes
- Daily totals are most accurate with **Usage access** granted. Without it, they are estimates.
- Some phone makers add their own background limits. You may also need to allow Speed Meter to run in the background in your phone's settings.
- Start on boot needs notification permission to show the indicator. If it was denied, allow notifications in your phone's settings.
- This release still uses a pre-release (alpha) version of Material 3, so minor visual quirks may appear.

Thanks for using **Speed Meter**! 🚀
