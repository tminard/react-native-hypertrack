
import { NativeModules } from 'react-native';

const { RNHyperTrack } = NativeModules;

module.exports = {
    // Method to intialize driver SDK with token(Publishable Key)
    initialize(token) {
        RNHyperTrack.initialize(token);
    },

    // get active publishable key
    getPublishableKey(callback) {
        RNHyperTrack.getPublishableKey(callback);
    },

    // create a new user
    createUser(name, callback) {
        RNHyperTrack.createUser(driverId);
    },

    // set a user with id
    setUserId(userId) {
        RNHyperTrack.setUserId(driverId);
    },

    // get current user id
    getUserId(callback) {
        RNHyperTrack.getUserId(driverId);
    },

    // start tracking
    startTracking(callback) {
        RNHyperTrack.startTracking(callback);
    },

    // start tracking
    stopTracking(callback) {
        RNHyperTrack.stopTracking(callback);
    },

    // get tracking status
    isTracking(callback) {
        RNHyperTrack.isTracking(callback);
    },

    // Method to complete an action
    completeAction(actionId, callback) {
        RNHyperTrack.completeAction(actionId, callback);
    }
}
