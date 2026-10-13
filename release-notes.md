# Speed Meter v0.7.1: Usage Access Switch and Display Polish

**Speed Meter v0.7.1** makes Usage access easier to manage and tidies up how speeds and the status bar icon look.

---

## What's New

### 🔘 Usage Access Switch
- **Switch in More → Notification layout.** The Usage access row now has a switch that shows whether access is on.
- **Updates on return.** When you come back from system settings, the switch shows the current state.
- **Quick access.** Tap the row or its switch to open the Usage access setting.

### 🔢 Cleaner Numbers
- **Speed values** show whole numbers for B/s and KB/s, and one decimal place for MB/s and above.
- **Data totals** show whole bytes, and two decimal places for KB and above.

### 🖼️ Better Status Bar Icon
- Long values and units shrink to fit the icon.
- The value and unit are centred vertically.

### ✨ Polish
- The custom switch shows a cross-fading check and close icon as you slide it.
- Notification text is slightly larger and sits closer to the edges.
- Unit option rows in dialogs have a neater tap area.

---

## Under the Hood
- Simplified the usage access check and network type handling.
- Version bumped to **0.7.1**.

---

## Permissions
- **Usage access** (optional) lets the app read Android's data usage records for exact daily totals. Turn it on in **More → Notification layout**. The app does not send your data anywhere.

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
