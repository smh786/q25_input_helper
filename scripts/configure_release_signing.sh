#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat >&2 <<'EOF'
Usage:
  scripts/configure_release_signing.sh <keystore.jks> <key-alias>

Required environment variables:
  KEYSTORE_PASSWORD
  KEY_PASSWORD

This stores the release signing material as GitHub Actions secrets for the
current repository using the GitHub CLI.
EOF
}

if [ "$#" -ne 2 ]; then
  usage
  exit 2
fi

keystore_path="$1"
key_alias="$2"

if [ ! -f "$keystore_path" ]; then
  echo "Keystore not found: $keystore_path" >&2
  exit 1
fi

: "${KEYSTORE_PASSWORD:?Set KEYSTORE_PASSWORD before running this script.}"
: "${KEY_PASSWORD:?Set KEY_PASSWORD before running this script.}"

tmp_base64="$(mktemp)"
trap 'rm -f "$tmp_base64"' EXIT

if command -v keytool >/dev/null 2>&1; then
  keytool -list \
    -keystore "$keystore_path" \
    -storepass "$KEYSTORE_PASSWORD" \
    -alias "$key_alias" >/dev/null
fi

base64 "$keystore_path" | tr -d '\n' > "$tmp_base64"

gh secret set KEYSTORE_BASE64 < "$tmp_base64"
printf '%s' "$KEYSTORE_PASSWORD" | gh secret set KEYSTORE_PASSWORD
printf '%s' "$key_alias" | gh secret set KEY_ALIAS
printf '%s' "$KEY_PASSWORD" | gh secret set KEY_PASSWORD

echo "Release signing secrets configured."
