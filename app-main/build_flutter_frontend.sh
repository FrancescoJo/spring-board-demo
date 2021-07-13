#!/bin/bash
CURRENT_DIR=`pwd`

pushd $CURRENT_DIR > /dev/null

cd src/frontend

flutter build web $1

if [ "$?" -eq "1" ]; then
    popd
    echo ERROR: Please follow instructions below to troubleshoot if:
    echo   - 'flutter' command is not found, read https://flutter.dev/docs/get-started/install/windows
    echo   - 'flutter build web' is failed , read https://flutter.dev/docs/get-started/web
    exit 1
fi

cp -r build/web/* $CURRENT_DIR/src/main/resources/static/

popd
