#! /bin/bash
set -e

if [[ "$TRAVIS_PULL_REQUEST" == "false" && "$TRAVIS_BRANCH" == "scalajs-hypertino-snapshot" ]]; then
  echo "$key_password" | gpg --passphrase-fd 0 ./travis/ht-oss-public.asc.gpg
  echo "$key_password" | gpg --passphrase-fd 0 ./travis/ht-oss-private.asc.gpg

  if grep "buildVersion\s*=.*-SNAPSHOT" build.sbt; then
    sbt test +coreJVM/publishSigned +coreJS/publishSigned +scalatestSupportJVM/publishSigned +scalatestSupportJS/publishSigned
  else
    sbt test +coreJVM/publishSigned +coreJS/publishSigned +scalatestSupportJVM/publishSigned +scalatestSupportJS/publishSigned sonatypeReleaseAll
  fi
else
  sbt test
fi
