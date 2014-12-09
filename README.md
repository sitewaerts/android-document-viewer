cleverdox Viewer for Android
============================

Free Android Document Viewer App required by https://github.com/sitewaerts/cordova-plugin-document-viewer.

**This project is currently under development and not yet ready to use.**

The App is available at Google play: https://play.google.com/store/apps/details?id=de.sitewaerts.cleverdox.viewer

## Requirements ##

* Android 4.1+
* MuPDF Android 1.5 (an upgraded to the most recent version is planned, but we want to use some features of https://github.com/sitewaerts/zReader-mupdf which is based on 1.5)

## Building the app ##

1. Build MuPDF 1.5
	1. download mupdf-1.5-source.tar.gz from http://mupdf.com/downloads/archive/
	2. fix this bug http://stackoverflow.com/questions/24209654/error-while-compiling-mupdf-1-5-using-android-ndk
	3. follow instructions in platform/android/ReadMe.txt
	4. add the android folder to your IDE as an Android project/module (Eclipse: Import -> Android -> Existing Android Code Into Workspace. If there is an error, try checking "Copy projects into workspace")
	5. make it an Android Library (Eclipse: Project -> Properties -> Android -> Library -> Is Library)
	6. HOTFIX: edit com.artifex.mupdfdemo.ReaderView getSelectedView() and replace the exception with "return null;"
2. Add the content of this repository to your IDE as an Android project/module
	1. update the reference to the MuPDF Library (Eclipse: Project -> Properties -> Android -> Library -> Remove/Add)


## Credits ##

This App is based on muPDF:

http://www.mupdf.com/

http://git.ghostscript.com/?p=mupdf.git;a=summary

