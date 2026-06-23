# Q25 Input Helper

Targeted Android input compatibility fixes for the Zinwa Q25 / BenOS.

This app is for screens where the Q25 physical keyboard does not naturally produce the input the user expects. It is intentionally small, scoped, and conservative.

## Scope

Q25 Input Helper may:

- Detect a known lockscreen or system-app input surface.
- Translate Q25 physical key presses into the correct UI input for that surface.
- Wake the sleeping screen from the Q25 space bar.
- Click only positively identified accessibility nodes.
- Do nothing when the target screen cannot be proven.

Q25 Input Helper must not:

- Globally remap keys.
- Consume space bar input while the screen is already awake.
- Act only because the device is locked.
- Consume keys on unknown screens.
- Log PINs, passwords, typed content, or user input.
- Patch unrelated app behavior.

## Current Fixes

- Sleeping screen wake.
  - Handles only the Q25 space bar.
  - Requires the display to be non-interactive before acting.
  - Acquires a short wake lock to turn the screen on.
  - Ignores all other keys and leaves space untouched while the screen is awake.
- SystemUI PIN screen.
  - Detects `com.android.systemui:id/keyguard_pin_view`.
  - Requires the active root package to be `com.android.systemui`.
  - Requires the device keyguard to be locked.
  - Maps Q25 PIN keys to SystemUI PIN keypad buttons.
  - Requires the target button to belong under the PIN view before clicking.
- Stock Calculator keypad.
  - Requires the active root package to be a supported stock calculator package.
  - Maps Q25 number, operator, parentheses, percent, factorial, decimal, delete, and Sym keys.
  - Maps Sym to the stock calculator's scientific button toggle.
  - Leaves return for the calculator app to handle and maps delete to backspace.
  - Clicks only known stock calculator keypad buttons.

## Key Mappings

### SystemUI PIN

These mappings only apply on the Android lockscreen PIN keypad.

| Q25 key | PIN input |
| --- | --- |
| `w` or `1` | `1` |
| `e` or `2` | `2` |
| `r` or `3` | `3` |
| `s` or `4` | `4` |
| `d` or `5` | `5` |
| `f` or `6` | `6` |
| `z` or `7` | `7` |
| `x` or `8` | `8` |
| `c` or `9` | `9` |
| `0` | `0` |
| Enter or D-pad center | Submit |
| Delete | Backspace |

### Stock Calculator

These mappings only apply in supported stock calculator packages:
`com.google.android.calculator`, `com.android.calculator2`, and `com.android.calculator`.

| Q25 key | Calculator input |
| --- | --- |
| `w` or `1` | `1` |
| `e` or `2` | `2` |
| `r` or `3` | `3` |
| `s` or `4` | `4` |
| `d` or `5` | `5` |
| `f` or `6` | `6` |
| `z` or `7` | `7` |
| `x` or `8` | `8` |
| `c` or `9` | `9` |
| `0` | `0` |
| `i` | Minus |
| `o` | Plus |
| `a` | Multiply |
| `g` | Divide |
| `m` | Decimal point |
| `u` | Percent |
| `b` | Factorial |
| `t` | Left parenthesis |
| `y` | Right parenthesis |
| Sym | Show or hide scientific buttons |
| Delete | Backspace |

Return/enter is intentionally left for the calculator app to handle.

### Sleeping Screen Wake

These mappings only apply while the display is off or sleeping.

| Q25 key | Action |
| --- | --- |
| Space | Wake screen |

This fix is inspired by and credits mionica's closed Q25 KeyMapper Boot Fix PR:
https://github.com/smh786/q25-keymapper-boot-fix/pull/7

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

Calculator key mapping has local simulation coverage in the unit test suite. It does not require a phone or emulator:

```bash
./gradlew testStandardDebugUnitTest --tests com.q25.inputhelper.input.CalculatorInputSimulationTest
```

The `E2E` workflow can be run manually from GitHub Actions. It starts an emulator, installs the app, and verifies the target package. Device-specific Q25 behavior still needs manual testing on hardware.

For hardware testing:

```bash
./scripts/install-system-test.sh --apk ./q25-input-helper-system-0.3.0.apk
```

or on Windows:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\install-system-test.ps1 -ApkPath .\q25-input-helper-system-0.3.0.apk
```

## Contributing

Read [CONTRIBUTING.md](CONTRIBUTING.md) before opening a PR.

The short version:

- Each input fix must be narrowly scoped.
- Each fix must prove the target screen before acting.
- Each fix needs tests.
- PRs must bump `versionName` using SemVer.
- CI must pass before merge.
