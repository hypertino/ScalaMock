#! /bin/bash
set -e

if [[ "$TRAVIS_PULL_REQUEST" == "false" && "$TRAVIS_BRANCH" == "scalajs-hypertino-snapshot" ]]; then
  echo "$key_password" | gpg --passphrase-fd 0 ./travis/ht-oss-public.asc.gpg
  echo "$key_password" | gpg --passphrase-fd 0 ./travis/ht-oss-private.asc.gpg
  export SBT_OPTS=-XX:MaxMetaspaceSize=512m
  if grep "version in .*-SNAPSHOT" version.sbt; then
    sbt ++$TRAVIS_SCALA_VERSION test scalamock-coreJVM/publishSigned scalamock-coreJS/publishSigned scalamock-scalatest-supportJVM/publishSigned scalamock-scalatest-supportJS/publishSigned
  else
    sbt ++$TRAVIS_SCALA_VERSION test scalamock-coreJVM/publishSigned scalamock-coreJS/publishSigned scalamock-scalatest-supportJVM/publishSigned scalamock-scalatest-supportJS/publishSigned sonatypeReleaseAll
  fi
else
  sbt ++$TRAVIS_SCALA_VERSION test
fi
