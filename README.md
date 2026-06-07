# Q25 Input Helper

Targeted Android input compatibility fixes for the Zinwa Q25 / BenOS.

This app is for screens where the Q25 physical keyboard does not naturally produce the input the user expects. It is intentionally small, scoped, and conservative.

## Scope

Q25 Input Helper may:

- Detect a known lockscreen or system-app input surface.
- Translate Q25 physical key presses into the correct UI input for that surface.
- Click only positively identified accessibility nodes.
- Do nothing when the target screen cannot be proven.

Q25 Input Helper must not:

- Globally remap keys.
- Act only because the device is locked.
- Consume keys on unknown screens.
- Log PINs, passwords, typed content, or user input.
- Patch unrelated app behavior.

## Current Fixes

- SystemUI PIN screen scaffold.
  - Detects `com.android.systemui:id/keyguard_pin_view`.
  - Maps Q25 PIN keys to SystemUI PIN keypad buttons.
  - Requires the target button to belong under the PIN view before clicking.

Planned fixes can include targeted adapters for broken system-app input surfaces, such as dialer/phone screens where number entry does not work correctly from the Q25 keyboard.

## Variants

- `standard`: sideload build with a launcher activity for testing.
- `system`: no-launcher build intended for ROM `/system/priv-app` inclusion.

## Build

```bash
./gradlew testStandardDebugUnitTest testSystemDebugUnitTest
./gradlew lintStandardDebug lintSystemDebug
./gradlew assembleStandardDebug assembleSystemDebug
./gradlew assembleStandardRelease assembleSystemRelease
```

## E2E Smoke Test

The `E2E` workflow can be run manually from GitHub Actions. It starts an emulator, installs the app, and verifies the target package. Device-specific Q25 behavior still needs manual testing on hardware.

For hardware testing:

```bash
./scripts/install-system-test.sh --apk ./q25-input-helper-system-0.1.0.apk
```

or on Windows:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\install-system-test.ps1 -ApkPath .\q25-input-helper-system-0.1.0.apk
```

## Contributing

Read [CONTRIBUTING.md](CONTRIBUTING.md) before opening a PR.

The short version:

- Each input fix must be narrowly scoped.
- Each fix must prove the target screen before acting.
- Each fix needs tests.
- PRs must bump `versionName` using SemVer.
- CI must pass before merge.
