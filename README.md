# react-native-hypertrack
React native module for hypertrack-android and hypertrack-ios SDKs

[![Slack Status](http://slack.hypertrack.io/badge.svg)](http://slack.hypertrack.io) [![npm version](https://badge.fury.io/js/react-native-hypertrack.svg)](https://badge.fury.io/js/react-native-hypertrack)

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

## Usage
```javascript
import RNHyperTrack from 'react-native-hypertrack';

// TODO: What do with the module?
RNHyperTrack;
```

## Documentation
The HyperTrack documentation is at [docs.hypertrack.io](http://docs.hypertrack.io/).

## Support
For any questions, please reach out to us on [Slack](http://docs.hypertrack.io/) or on help@hypertrack.io. Please create an [issue](https://github.com/hypertrack/hypertrack-cordova/issues) for bugs or feature requests.

## Acknowledgements
Thanks to [react-native-create-library](https://github.com/frostney/react-native-create-library) which saved a few hours.
