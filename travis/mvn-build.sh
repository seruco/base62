#! /usr/bin/env bash

function snapshot() {
    echo "snapshot build"
    mvn clean package javadoc:jar source:jar-no-fork deploy -V -U --settings travis/settings.xml
}

function release() {
    echo "release build"
    mvn clean package javadoc:jar source:jar-no-fork deploy -V -U --settings travis/settings.xml
}

function build() {
    echo "build"
    mvn clean install -V -U --settings travis/settings.xml
}

function main() {
    if [[ $TRAVIS_PULL_REQUEST != "false" ]]; then build;
    elif [[ $TRAVIS_BRANCH == "develop" ]]; then snapshot;
    elif [[ $TRAVIS_BRANCH == "master" ]]; then release;
    else build;
    fi;
}

# Run main if this is not included
if ((${#BASH_SOURCE[@]} == 1)); then
    main "$@"
fi
