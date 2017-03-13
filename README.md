# react-native-hypertrack
React native module for hypertrack-android and hypertrack-ios SDKs. Methods in the Driver SDK are covered in the current release. The [example-react-native](https://github.com/hypertrack/example-react-native) app is built on top of this module.

[![Slack Status](http://slack.hypertrack.io/badge.svg)](http://slack.hypertrack.io) [![npm version](https://badge.fury.io/js/react-native-hypertrack.svg)](https://badge.fury.io/js/react-native-hypertrack)

## What's new in v0.4.+
The v0.4.+ wrapper is built for HyperTrack v3, and will not work with the older SDKs. There will be breaking changes if you are upgrading - please refer to [docs.hypertrack.com](https://docs.hypertrack.com).

## Getting started
In your project directory, install and link the module package from npm.
```
$ npm install react-native-hypertrack --save
$ react-native link react-native-hypertrack
```

If you are using an older version of React Native that does not support `link`, you can [manually link](https://facebook.github.io/react-native/docs/linking-libraries-ios.html) libraries.

### Android setup
To use the HyperTrack Android SDKs, the following urls need to be added to your `android/build.gradle` file. This will configure the repository urls for the SDKs.

```
allprojects {
    repositories {
        ...
        maven { url 'http://hypertrack-android-sdk.s3-website-us-west-2.amazonaws.com/' }
        maven { url 'https://repo.eclipse.org/content/repositories/paho-releases/' }
    }
}
```

In your `android/app/build.gradle` file, update the following:

```
    compileSdkVersion 25
    buildToolsVersion "25.0.2"
```

If you have some issues with Android, some common troubleshooting is [here](android-troubleshooting.md).

### iOS setup
1. The native iOS SDKs need to be setup using Cocoapods. In your project's `ios` directory, create a Podfile.
    ```
    $ cd ios
    $ pod init
    ```

2. Edit the Podfile to include `HTTransmitter` as a dependency for your project, and then install the pod.
    ```
    $ cat Podfile
    target 'YourApp' do
      pod 'HTTransmitter'
    end

    $ pod install
    ```

3. Now, open the iOS project with the `.xcworkspace` file in Xcode, and add the native SDK `.a` files in the linked frameworks and libraries section. You need to add *libHTTransmitter.a*, *libHTCommon.a*, *libMQTTClient.a*.
![Linked frameworks and libraries](readme-imgs/linker.png)

## Usage

#### Import and initialize SDK with your Publishable key before making any other API call

```javascript
 import RNHyperTrack from 'react-native-hypertrack';
 ...

 export default class MyApp extends Component {
   constructor() {
     super();
     // Initialize HyperTrack wrapper
     RNHyperTrack.initialize(config.HT_PUBLISHABLE_KEY);
   }
 }
  ...
```
#### Starting a Trip
```javascript
 RNHyperTrack.startTrip(
   DRIVER_ID,
   [TASK_ID_1, TASK_ID_2...],
   (successValue) => {
     // Handle Success
   },
   (error) => {
     // Handle error
   }
 );
```
#### Completing a task

```javascript
 RNHyperTrack.completeTask(
   TASK_ID,
   (successValue) => {
      // Handle Success
   },
   (error) => {
     // Handle error
   }
 );
```

#### Ending a trip
```javascript
 RNHyperTrack.endTrip(
   TRIP_ID,
   (successValue) => {
     // Handle success
   },
   (error) => {
     // Handle error
   }      
 );
```
## Documentation
The HyperTrack documentation is at [docs.hypertrack.io](http://docs.hypertrack.io/).

## Support
For any questions, please reach out to us on [Slack](http://docs.hypertrack.io/) or on help@hypertrack.io. Please create an [issue](https://github.com/hypertrack/hypertrack-cordova/issues) for bugs or feature requests.

## Acknowledgements
Thanks to [react-native-create-library](https://github.com/frostney/react-native-create-library) which saved a few hours.
