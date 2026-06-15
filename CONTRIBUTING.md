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
configuration is caught before merge.

If this is the first release key for the project, create one with:

```bash
scripts/create_release_keystore.sh
```

That creates `.release/q25-input-helper-release.p12` and
`.release/release-signing.env`. Keep both files backed up privately; Android
updates require future releases to use the same signing key.

Configure the GitHub secrets with:

```bash
set -a; . .release/release-signing.env; set +a
scripts/configure_release_signing.sh "$KEYSTORE_PATH" "$KEY_ALIAS"
```
