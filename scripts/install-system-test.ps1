param(
    [string]$ApkPath = "",
    [string]$AdbPath = "adb",
    [switch]$CleanInstall
)

$ErrorActionPreference = "Stop"

function Resolve-ApkPath {
    param([string]$PathFromUser)

    if ($PathFromUser) {
        return (Resolve-Path -LiteralPath $PathFromUser).Path
    }

    $candidate = Get-ChildItem -Path (Get-Location) -Filter "q25-input-helper-system-*.apk" |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1

    if (-not $candidate) {
        throw "Pass -ApkPath or run this from a folder containing q25-input-helper-system-*.apk"
    }

    return $candidate.FullName
}

$apk = Resolve-ApkPath $ApkPath
$packageName = "com.q25.inputhelper"
$activity = "$packageName/.MainActivity"

Write-Host "Using APK: $apk"
& $AdbPath devices

if ($CleanInstall) {
    Write-Host "Uninstalling existing package, if present..."
    & $AdbPath uninstall $packageName | Out-Host
}

Write-Host "Installing no-launcher system-test APK..."
& $AdbPath install -r $apk | Out-Host

Write-Host "Launching once to clear Android's stopped/notLaunched state..."
& $AdbPath shell am start -n $activity | Out-Host

Write-Host "Checking for launcher entry; expected result is no output below:"
& $AdbPath shell cmd package query-activities --brief -a android.intent.action.MAIN -c android.intent.category.LAUNCHER |
    Select-String $packageName

Write-Host "Package state:"
& $AdbPath shell dumpsys package $packageName |
    Select-String "versionName|stopped|notLaunched|InputAccessibilityService"

Write-Host ""
Write-Host "Next test:"
Write-Host "  Enable the Q25 Input Helper accessibility service."
Write-Host "  Reproduce the target input screen."
Write-Host "  adb shell settings get secure enabled_accessibility_services"
Write-Host ""
Write-Host "Note: this is only a no-launcher user-app test. ROM integration should install the APK as /system/priv-app."
