#!/bin/bash

delete_all_apps () {
    adb devices | tail +2 | xargs -n 2 | xargs -n 1 | head -n 1 | xargs -t -I {} adb -s {} uninstall $1
}

delete_all_apps "com.androidopengl.app"