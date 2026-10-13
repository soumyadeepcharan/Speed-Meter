# Speed Meter v0.8.0: Smarter Permissions and Cleaner Help

**Speed Meter v0.8.0** asks for notification permission only when you turn the indicator on, tidies up the status bar notification, and turns Help & feedback into a quick bottom sheet.

---

## What's New

### 🔔 Notification Permission When You Need It
- **Asked when you turn the indicator on.** Speed Meter no longer asks for notification permission the moment you open the app.
- **Indicator waits for permission.** If you say no, the indicator stays off until you allow notifications.
- **Quieter first launch.** If the indicator is off, the app doesn't ask for anything.

### 📶 Tidier Notification
- The notification now shows **Down**, **Up**, **Data**, and **WiFi** in a single row.
- Each value sits in its own column, so the layout stays steady as numbers change.

### 🖼️ Status Bar Icon
- The status bar icon uses a fixed layout, with the speed and unit placed for a clear look.

### 💬 Help & Feedback Sheet
- **Help & feedback** opens a small bottom sheet with **Suggest a feature** and **Report a bug**.
- Choosing either option opens your email app with a message ready to send.

### 🎚️ Smoother Switch
- The custom switch moves a little faster and settles more quickly.

### 🧩 Dialogs Stay in View
- Dialogs now stay clear of the status bar and navigation bar.

---

## Under the Hood
- Notification updates reuse one layout object, which reduces work every second.
- The app's signing files are excluded from source control again.
- Version bumped to **0.8.0**.

---

## Permissions
- **Notifications** are requested when you turn the indicator on (Android 13 and newer).
- **Usage access** (optional) lets the app read Android's data usage records for exact daily totals. Turn it on in **More → Notification layout**. The app does not send your data anywhere.

---

## Compatibility
- Requires **Android 8.0 (Oreo) or newer**.
- Release builds are signed with **v1, v2, v3 and v4** signature schemes.

---

## Known Notes
- Without Usage access, daily totals can read lower after a phone restart.
- Some devices name their Wi-Fi network interface differently. On those, Wi-Fi shows 0 until Usage access is granted.
- Very long speed values may be clipped in the status bar icon.
- This release still uses a pre-release (alpha) version of Material 3, so minor visual quirks may appear.

Thanks for using **Speed Meter**! 🚀
