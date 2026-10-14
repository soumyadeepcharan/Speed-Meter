# Speed Meter v5.0.0

🎉 **Speed Meter v5.0.0 adds an expanding top bar, a softer scrim behind the Help sheet, and a cleaner bottom edge, and it updates the build tools.**

---

## What's New

### 📱 Expanding top bar
- Pull down on the header to expand the top bar. The app name grows into a larger title as you pull.
- Release partway and the header settles open or closed on its own.
- The Quit button moves with the header, so it stays easy to reach.

### 🌫️ Scrim behind the Help sheet
- When the Help & feedback sheet is open, the page behind it dims softly so the sheet stands out.
- The sheet's bottom edge matches the navigation bar, so the transition looks seamless.

### 🧭 Cleaner bottom edge
- The area behind the navigation bar now blends with the page or sheet color.
- The settings page no longer stretches past its edge when you scroll to the end.

### 🔤 Standard text styling
- Text uses Material 3's standard styles, so it matches the rest of your system.

### 🔔 Richer notification
- The notification now includes an expanded layout, so the four values stay readable in the expanded view.
- The notification appears immediately when monitoring starts.

---

## Under the Hood
- Build tools updated to Android Gradle Plugin 9.3.1, Kotlin 2.3.21, and Gradle 9.6.1.
- Kotlin support is built into the Android Gradle Plugin.
- Settings sections align with your phone's corner radius again.
- Version bumped to **5.0.0** (version code 16).

---

## Changes to Know About
- Pulling down on the header changes the layout of the top bar. Tap Quit or pull back up to return it to normal.
- Settings section titles line up with your screen corners again.

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
