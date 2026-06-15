# Contributing

Q25 Input Helper is a compatibility layer, not a general key remapper.

## PR Rules

Every PR must:

- Explain the broken user experience.
- Name the affected app/screen.
- Include the exact package name and view IDs used for detection.
- Prove the target screen before consuming or translating keys.
- No-op when the target screen is not detected.
- Avoid logging user input, PINs, passwords, or typed text.
- Add or update tests.
- Bump `versionName` in `app/build.gradle` using SemVer.

## Fix Design

Prefer one class per input surface:

- `SystemUiPinInputFix`
- `PhoneDialerInputFix`
- `SomeSystemAppInputFix`

Each fix should implement `InputFix` and be registered in `InputAccessibilityService`.

Good fixes are:

- Screen-specific.
- View-ID specific.
- Small enough to reason about.
- Safe when the screen changes unexpectedly.

Bad fixes are:

- Global key remaps.
- Device-locked checks without screen detection.
- Broad package-only checks.
- Anything that records or logs user input.

## Required Checks

PRs run:

- version bump check
- unit tests
- Android lint
- debug APK build

The manual `E2E` workflow runs an emulator smoke test. Run it for changes that affect installation, app startup, accessibility service registration, or Android framework integration.

Release builds run after `main` passes tests.

## Release Signing

The release workflow requires these repository Actions secrets:

- `KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

PRs run a `Release signing secrets configured` check so missing signing
configuration is caught before merge. Configure the secrets with:

```bash
export KEYSTORE_PASSWORD="..."
export KEY_PASSWORD="..."
scripts/configure_release_signing.sh ./release.jks q25-input-helper
```
