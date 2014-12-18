building the app with gradle
============================

The required MuPDF code is managed in a separate repository: https://github.com/sitewaerts/mupdf-for-cleverdox

Before you continue, make sure to download both the app repository and the MuPDF repository and put both into the same directory. The gradle files of this script expect MuPDF to be in a folder named "mupdf-for-cleverdox".

Also, you need to have an environment variable ANDROID_HOME which points to the directory of the Android sdk (and, of course JAVA_HOME for the jdk)

The app was developed with the following Android sdk version:
```
compileSdkVersion 21
buildToolsVersion "21.1.1"
```
so either make sure that your Android sdk has this version available or modify build.gradle in the android-document-viewer project to use your most recent version.

## execute gradle ##

Windows: 
```
gradlew.bat assembleDebug
gradlew.bat assembleRelease
```

Linux/OS X: 
```
gradlew assembleDebug
gradlew assembleRelease
```

release builds need to be signed separately