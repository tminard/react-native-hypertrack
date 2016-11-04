# react-native-hypertrack
React native module for hypertrack-android and hypertrack-ios SDKs

[![Slack Status](http://slack.hypertrack.io/badge.svg)](http://slack.hypertrack.io) [![npm version](https://badge.fury.io/js/react-native-hypertrack.svg)](https://badge.fury.io/js/react-native-hypertrack)

## Getting started
```
$ npm install react-native-hypertrack --save
```

### Mostly automatic installation
```
$ react-native link react-native-hypertrack
```

### Android setup
To use the Android SDKs, the following urls need to be added to your `android/build.gradle` file

```
allprojects {
    repositories {
        ...
        maven { url 'http://hypertrack-android-sdk.s3-website-us-west-2.amazonaws.com/' }
        maven { url 'https://repo.eclipse.org/content/repositories/paho-releases/' }
    }
}
```

### Manual installation

#### iOS
1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-hypertrack` and add `RNHyperTrack.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNHyperTrack.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android
1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNHyperTrackPackage;` to the imports at the top of the file
  - Add `new RNHyperTrackPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-hypertrack'
  	project(':react-native-hypertrack').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-hypertrack/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-hypertrack')
  	```

## Usage
```javascript
import RNHyperTrack from 'react-native-hypertrack';

// TODO: What do with the module?
RNHyperTrack;
```
