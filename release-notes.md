# Speed Meter v0.5.0: Data Usage and Theme Choice

**Speed Meter v0.5.0** shows how much mobile and Wi-Fi data you use right in your status bar notification. You can also now choose light, dark, or system theme from the More screen.

---

## What's New

### 📊 Data Usage in the Notification
- **Mobile data and Wi-Fi data.** The status bar notification shows how much **mobile data** and **Wi-Fi data** you have used since the indicator started.
- **Readable sizes.** Amounts show as B, KB, MiB, or GiB, depending on size.
- **Wi-Fi is an estimate.** The Wi-Fi figure is calculated from your phone's total traffic minus mobile traffic.
- **Resets when the indicator restarts.** Totals start again after a reboot, a force stop, or turning the indicator off and on. Daily totals are coming in a future release.

### 🎨 Choose Your Theme
- Open **More → Choose theme** and pick **System default**, **Light mode**, or **Dark mode**.
- Your choice is saved and applied the next time you open the app.
- This replaces the dark theme switch on the More screen.

### 🏠 Simpler Home Screen
- The **Indicator** section now shows one **Enable indicator** row. Tap anywhere on the row to turn the indicator on or off.

### 🖼️ Refreshed Status Bar Icon and Notification
- The status bar icon is taller, with larger speed numbers and a bolder unit label.
- The notification layout has a compact speed icon and one details line: **Data**, **WiFi**, **Down**, and **Up**.

---

## Under the Hood
- The speed service reads your indicator setting once on launch. It no longer restarts when you change the setting while the app is open.
- All text in the app, including the feedback email templates, now lives in one place for easier translation.
- Version bumped to **0.5.0**.

---

## Compatibility
- Requires **Android 8.0 (Oreo) or newer**.
- Release builds are signed with **v1, v2, v3 and v4** signature schemes.

---

## Known Notes
- Data totals reset when the indicator restarts. They are not saved from day to day yet.
- The Wi-Fi figure is an estimate, and may include some traffic from other non-mobile networks.
- This release still uses a pre-release (alpha) version of Material 3, so minor visual quirks may appear.

Thanks for using **Speed Meter**! 🚀
