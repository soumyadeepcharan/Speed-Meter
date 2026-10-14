# Speed Meter

<p align="center">
  <img src="assets/ic_launcher.svg" width="120" height="120" alt="Speed Meter icon"/>
</p>

**Speed Meter** is a lightweight Android app that shows your internet speed in your status bar. You don't need to open the app to see how fast your connection is. The speed stays visible in a single notification line.

---

## What It Does

### Live speed in your status bar
- Shows your current **download** and **upload** speed, updated every second.
- Shows **today's data usage** for mobile data and WiFi, separately.
- Each value has a small icon in front of it (down arrow, up arrow, data, and WiFi), so you can tell them apart at a glance.
- Everything fits on one line in a notification that doesn't expand.
- The notification appears right away when monitoring starts.
- The notification color adapts to light or dark mode on older Android versions.
- The status bar icon shows a live number: total speed, download only, or upload only. You choose.

### Turn it on or off
- One switch at the top of the settings page starts or stops the whole feature.
- It's off when you first install the app.
- Android asks for notification permission only when you turn the indicator on.
- The **Quit** button in the top bar stops monitoring and removes the notification straight away.

### Find the settings easily
- Pull down from the top of the settings page to open the expanded header. The app name appears in the larger header, and the page dims slightly behind it.
- Keep pulling to open it fully, or let go partway and it settles open or closed on its own.
- Pull back up to return the header to normal.
- Scroll the settings page as usual. The header only expands when the page is at the top.

### Choose how numbers look
- **Speed units:** bytes per second (KB/s, MB/s) or bits per second (Kb/s, Mb/s).
- **Data usage units:** bits, decimal bytes (kB, MB; 1000-based), or binary bytes (KiB, MiB; 1024-based).
- **Icon speed:** total, download only, or upload only.

### Accurate daily data usage
- Counts mobile and WiFi data used **today**, starting from midnight.
- If you grant **Usage access**, totals come from Android's own usage statistics. This is the most accurate option.
- Without it, the app still estimates usage from a daily starting point, so you always see a number.

### Everyday convenience
- **Start on boot:** restarts monitoring automatically after your phone restarts, even if you quit the app or turned the indicator off before shutting down.
- **Battery optimization:** one tap exempts Speed Meter so Android doesn't stop it in the background. To turn optimization back on, switch the option off. It opens the same battery settings screen so you can restore it.
- **Notification settings:** opens Android's notification settings for the app.

### Looks good and feels native
- Built with Material Design 3, including its expressive style. Buttons change shape smoothly when pressed.
- On Android 12 and newer, colors match your wallpaper (Material You).
- Light mode, dark mode, or follow your phone's setting.
- Screens use the full display edge to edge. Corners follow your phone's screen shape where the phone reports it.
- The navigation bar area matches the screen background, so the bottom edge looks clean.
- The settings and Help screens use slightly narrower side margins, so more of each row is visible.
- The layout adapts to phones and tablets.
- The Help & feedback sheet has a visible drag handle, so you can see you can pull it down.
- The app follows your phone's rotation lock. If rotation is locked, the screen stays where it is.

### Help when you need it
- **Help & feedback** sheet: suggest a feature or report a bug. It opens a pre-filled email with your device details included.
- **About** screen: shows the current app version.
- Direct contact: **sdcgroupofficial@gmail.com**

---

## Requirements

- **Android:** 8.0 (Oreo) or newer.
- **Permissions:**
  - **Notifications:** shows the speed indicator.
  - **Usage access** (optional, recommended): gives the most accurate daily data totals.
  - **Battery optimization exemption** (optional): keeps monitoring running in the background.
  - **Start on boot** (optional): restarts monitoring after a restart.

---

## How to Use

1. Open the app and turn on the **indicator** switch at the top of the settings page.
2. Allow notifications when asked. The indicator appears right away.
3. Open **Notification settings** to choose:
   - Speed units
   - Data usage units
   - What speed the icon shows
4. Tap **Usage access** and grant it for the most accurate daily numbers.
5. Turn on **Start on boot** so monitoring resumes after a restart.
6. Turn off **Battery optimization** for Speed Meter so Android doesn't pause it.
7. Pick a **theme** (system, light, or dark) in General settings.
8. To stop everything quickly, tap **Quit** in the top bar.

---

## Troubleshooting

**The indicator isn't showing**
- Make sure the indicator switch at the top of the settings page is on.
- Make sure notifications are allowed for Speed Meter in Android's settings.

**Daily data looks wrong**
- Grant **Usage access** for accurate totals.
- Without it, usage is estimated from when the app started tracking that day.

**The indicator stops after a while**
- Turn off battery optimization for Speed Meter.
- Turn on **Start on boot** so it returns after a restart.
- Some phone makers add extra background limits. Check your phone's battery settings for Speed Meter.

**Long numbers are cut off in the notification**
- Values longer than the space available end with "…". Use a shorter unit (for example, MB/s instead of KB/s) to make them fit.

**The header won't expand**
- Scroll the settings page to the top first. The header only expands when the page is at the top.

**I tapped Quit by accident**
- Turn the indicator switch on again at the top of the settings page to start it.

---

## Support & Feedback

Found a bug or have an idea? We'd like to hear it.

📧 **Email:** sdcgroupofficial@gmail.com

You can also use **Help & feedback** in the app. It fills in your device details for you.

---

## License

This project is provided as-is. See the LICENSE file for details.

---

## Credits

Made with ❤️ from India by SDC Group
