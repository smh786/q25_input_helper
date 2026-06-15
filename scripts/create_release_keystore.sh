#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat >&2 <<'EOF'
Usage:
  scripts/create_release_keystore.sh [keystore.jks] [key-alias]

Defaults:
  keystore.jks: .release/q25-input-helper-release.p12
  key-alias:    q25-input-helper

The script writes a local .release/release-signing.env file containing the
generated passwords. Keep both files backed up privately; do not commit them.
EOF
}

if [ "${1:-}" = "-h" ] || [ "${1:-}" = "--help" ]; then
  usage
  exit 0
fi

keystore_path="${1:-.release/q25-input-helper-release.p12}"
key_alias="${2:-q25-input-helper}"
env_path="$(dirname "$keystore_path")/release-signing.env"

if [ -e "$keystore_path" ]; then
  echo "Keystore already exists: $keystore_path" >&2
  exit 1
fi

if ! command -v keytool >/dev/null 2>&1; then
  echo "keytool is required. Install a JDK or use the devcontainer." >&2
  exit 1
fi

random_secret() {
  if command -v openssl >/dev/null 2>&1; then
    openssl rand -base64 48 | tr -d '\n'
  else
    dd if=/dev/urandom bs=48 count=1 2>/dev/null | base64 | tr -d '\n'
  fi
}

mkdir -p "$(dirname "$keystore_path")"

keystore_password="${KEYSTORE_PASSWORD:-$(random_secret)}"
key_password="${KEY_PASSWORD:-$keystore_password}"

keytool -genkeypair \
  -keystore "$keystore_path" \
  -storetype PKCS12 \
  -storepass "$keystore_password" \
  -keypass "$key_password" \
  -alias "$key_alias" \
  -keyalg RSA \
  -keysize 4096 \
  -validity 10000 \
  -dname "CN=Q25 Input Helper, OU=Release, O=Q25 Input Helper, L=Unknown, ST=Unknown, C=GB"

chmod 600 "$keystore_path"

cat > "$env_path" <<EOF
export KEYSTORE_PASSWORD='$keystore_password'
export KEY_PASSWORD='$key_password'
export KEY_ALIAS='$key_alias'
export KEYSTORE_PATH='$keystore_path'
EOF
chmod 600 "$env_path"

echo "Created release keystore: $keystore_path"
echo "Created local signing env: $env_path"
echo
echo "To upload GitHub Actions secrets, run:"
echo "  set -a; . $env_path; set +a"
echo "  scripts/configure_release_signing.sh \"$keystore_path\" \"$key_alias\""
