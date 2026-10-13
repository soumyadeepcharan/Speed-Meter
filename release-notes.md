# Speed Meter v0.7.0: Daily Usage, Units, and a New Switch

**Speed Meter v0.7.0** adds **daily data usage** to the status bar notification, lets you choose **speed and data units**, and brings a new, easier-to-use switch.

---

## What's New

### 📅 Daily Data Usage
- **Today's totals.** The notification shows how much **mobile data** and **Wi-Fi data** you have used since midnight.
- **Exact numbers with Usage access.** Open **More → Notification layout** and allow **Usage access** for exact totals from Android.
- **Works without it, too.** Without Usage access, totals are estimated from your phone's data counters. They may read lower after a restart.

### 📏 Units You Can Choose
- **Speed units.** Show speeds in **bytes per second** (KB/s, MB/s, GB/s) or **bits per second** (Kb/s, Mb/s, Gb/s).
- **Data usage units.** Choose how sizes appear: bits (decimal), bytes (decimal, 1 MB = 1000 kB), or bytes (binary, 1 MiB = 1024 KiB, the default).
- Both settings are in **More → Notification settings**.

### 🔘 New Switch
- The **Enable indicator** switch is redrawn. You can tap it or drag it.

### 🧭 Clearer More Screen
- **Notification settings** now holds the speed and data unit options.
- **Notification layout** shows whether Usage access is granted.
- **General** holds the theme, help, and About items.

### 🖼️ Cleaner Notification
- The notification shows one line: **Down**, **Up**, **Data**, and **WiFi**.
- The status bar icon is slightly taller, with the speed value and unit centred.

### 🪟 Scrollable Dialogs
- Long dialogs scroll within half the screen height.

---

## Under the Hood
- Daily totals use Android's usage records when Usage access is granted. Otherwise they use a saved daily baseline.
- Wi-Fi is read from the Wi-Fi network interface in the fallback, instead of an estimate.
- Unit choices and daily totals are saved on your device.
- Version bumped to **0.7.0**.

---

## Permissions
- **Usage access** (optional) lets the app read Android's data usage records for exact daily totals. You can allow it in **More → Notification layout**. The app does not send your data anywhere.

---

## Compatibility
- Requires **Android 8.0 (Oreo) or newer**.
- Release builds are signed with **v1, v2, v3 and v4** signature schemes.

---

## Known Notes
- Without Usage access, daily totals can read lower after a phone restart.
- Some devices name their Wi-Fi network interface differently. On those, Wi-Fi shows 0 until Usage access is granted.
- This release still uses a pre-release (alpha) version of Material 3, so minor visual quirks may appear.

Thanks for using **Speed Meter**! 🚀
