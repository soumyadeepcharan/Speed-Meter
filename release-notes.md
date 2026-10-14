# Speed Meter v6.0.0

🎉 **Speed Meter v6.0.0 refines the pull-down header, gives notifications a clearer color on older Android versions, and tightens the layout margins.**

---

## What's New

### 📱 Smoother pull-down header
- Pull down from the top of the settings page to open the header. It follows your finger and settles open or closed when you let go.
- The header only opens when the page is at the top, so scrolling the settings stays smooth.
- Touches near the status bar and navigation bar are ignored, so system gestures keep working.

### 🔔 Notification color that adapts
- On Android 11 and older, the notification uses a color that matches light or dark mode.
- On Android 12 and newer, the notification uses the standard system look.

### 📐 Slimmer side margins
- The settings page and Help & feedback sheet use narrower side margins, so more of each row is visible.

---

## Under the Hood
- Header gestures are handled by a dedicated pointer layer, which keeps them separate from the settings scroll.
- The settings scroll position is shared with the header, so the header knows when the page is at the top.
- Notification color is chosen by Android version and night mode.
- Version bumped to **6.0.0** (version code 17).

---

## Changes to Know About
- Pull-down on the header only works when the settings page is scrolled to the top.
- Side margins on the settings and Help screens are slightly narrower than in v5.0.0.

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
