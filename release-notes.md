# Speed Meter v0.2.0: Foundation Update

Speed Meter v0.2.0 builds on v0.1.0 with a title bar, better screen handling, and a stronger release signing pipeline. Network speed monitoring is not included yet. It is the next step.

> **Note:** Version 0.2.0 does not measure speed yet. The screen below the title bar is currently empty.

---

## What's New

### 🧭 Top App Bar
- **Title bar** showing "Speed Meter" at the top of the screen.
- **Pinned scrolling.** The bar stays in place when content scrolls under it.

### 🖼️ Edge-to-Edge Display
- Content runs behind the status bar and navigation bar.
- Status bar and navigation bar icons switch between light and dark to match the theme.

### 📱 Better Screen Handling
- **Rotation follows the sensor.** The screen rotates with your device.
- **Keyboard-friendly layout.** The layout resizes when the on-screen keyboard appears.

### 🎨 Theming
- **Light and dark mode** follow your system setting.
- **Wallpaper colours (Android 12 and newer)** from Material You.

### 🔐 Stronger, Verified APK Signing
- Release APKs are signed with **v1, v2, v3, and v4 signature schemes** for compatibility across Android versions and install methods.
- Each release is **verified with apksigner** before publishing.
- The signing keystore is deleted after signing, so only the signed APK is published.

### ⚙️ Updated Dependencies and Tooling
- Compose BOM updated to **2026.02.01**.
- AndroidX Core KTX 1.16.0, Lifecycle Runtime KTX 2.9.0, and Activity Compose 1.10.1.
- Build targets **JVM 17** explicitly.
- GitHub Actions updated to the latest versions.

---

## Requirements

- Android **8.0 (Oreo)** or higher
- Works on phones and tablets

---

## Installation

1. Download the APK below for your device.
2. On your Android phone, go to **Settings → Install Unknown Apps** and allow installation from your browser or file manager.
3. Open the downloaded APK and tap **Install**.
4. Open **Speed Meter** from your app drawer.

---

**Full Changelog**: See `CHANGELOG.txt` for the tech stack and full technical details.
