#!/bin/bash

delete_all_apps () {
    adb devices | tail -n +2 | xargs -n 2 | cut -d ' ' -f1 | tail -n +1 | xargs -n 1 -t -I {} adb -s {} uninstall "$1"
}

echo -e "Deleting app from devices..."
delete_all_apps "com.androidopengl.app"