# Speed Meter v0.4.0: More Screen and Adaptive Layout

**Speed Meter v0.4.0** adds a **More** screen with notification settings, a dark theme switch, Help & feedback, and an About dialog. The layout now adapts to your screen: one page at a time on phones, and Home and More side by side on tablets and foldables.

> **Known issue:** If you turn the speed indicator off and then open the app, the indicator may start again. A fix is planned.

---

## What's New

### ⚙️ New "More" Screen
- **Notification settings.** Opens your phone's notification settings for Speed Meter, so you can manage the speed indicator in one tap.
- **Dark theme switch.** Switch between light and dark appearance from the **Appearance** section. It takes effect right away.
- **Help & feedback.** Choose one of two options:
  - **Suggest a feature**
  - **Report a bug**

  Either option opens your email app with a message ready to send to the developer. Bug reports include your app version, phone model and Android version, so you don't have to look them up.
- **About.** Shows the app name, version, and a short description.
- Settings are grouped into **General**, **Appearance**, and **Support** sections.

### 📱 Adaptive Layout
- **Phones.** The screen shows one page at a time. Use the floating bottom toolbar to switch between **Home** and **More**.
- **Tablets, foldables and large screens.** Home and More appear **side by side**, and the bottom toolbar is hidden.
- **Rotation and resizing.** The layout adjusts when you rotate your device or resize the window.

### ✨ Polish
- **Smoother app launch.** The system's blank launch preview is disabled, so the app opens more cleanly.
- **Accessible dialogs.** Dialogs take keyboard focus when they open, have screen reader labels, and show press feedback on buttons.

---

## Under the Hood
- Added the **Material 3 Adaptive** libraries (1.0.0) for the list-detail layout.
- Added **Material Icons Extended** for the More screen icons.
- Release builds now shrink unused resources. Debug builds install as a separate app with a `.debug` suffix.
- Version bumped to **0.4.0**.

---

## Compatibility
- Requires **Android 8.0 (Oreo) or newer**.
- Release builds are signed with **v1, v2, v3 and v4** signature schemes.

---

## Known Notes
- This release still uses a pre-release (alpha) version of Material 3, so minor visual quirks may appear.
- The speed indicator service starts every time the app opens. If you turned the indicator off, it may reappear after a relaunch. A fix is planned.

Thanks for using **Speed Meter**! 🚀
