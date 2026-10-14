# Speed Meter v4.0.0

🎉 **Speed Meter v4.0.0 steadies the build setup and returns the settings screen to its earlier layout.**

---

## What's New

### 🧭 Settings screen back to its classic layout
- The settings page is laid out as before: section titles sit at a standard indent, and the page stops above the navigation bar.
- The bottom bar used in v3.0.0 has been removed.
- The indicator switch remains at the top of the settings page.

### 🔤 Standard text styling restored
- Text uses the app's own body style again, matching the look of earlier releases.

---

## Changes to Know About
- Notifications no longer use the "appear immediately" foreground behavior added in v3.0.0. They appear as before.
- Settings section titles no longer line up with the screen corner radius. They use a standard indent.
- The navigation bar area is transparent again, and the settings page keeps its content clear of it.

---

## Under the Hood
- Build tools rolled back to Android Gradle Plugin 8.11.0, Kotlin 2.1.20, and Gradle 8.14.3 for a more stable build.
- The Kotlin Android plugin is applied again, with the JVM target set in the Kotlin options.
- Removed the debug symbol packaging rules added in v3.0.0.
- Version bumped to **4.0.0** (version code 15).

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
