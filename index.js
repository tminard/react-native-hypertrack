
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

    // connect SDK to the backend for a driver id
    connectDriver(driverId) {
        RNHyperTrack.connectDriver(driverId);
    },

    // get connected driver id
    getConnectedDriver(callback) {
        RNHyperTrack.getConnectedDriver(callback);
    },

    // get transmitting locations status
    isTransmitting(callback) {
        RNHyperTrack.isTransmitting(callback);
    },

    // Method to start trip in driver SDK
    startTrip(driverId, taskIds, success, failure) {
        RNHyperTrack.startTrip(driverId, taskIds, success, failure);
    },

    // Method to complete task in driver SDK
    completeTask(taskId, success, failure) {
        RNHyperTrack.completeTask(taskId, success, failure);
    },

    // Method to end trip in driver SDK
    endTrip(tripId, success, failure) {
        RNHyperTrack.endTrip(tripId, success, failure);
    },

    // Method to end all trips for driver in driver SDK
    endAllTrips(driverId, success, failure) {
        RNHyperTrack.endAllTrips(driverId, success, failure);
    },

    // Method to start shift in driver SDK
    startShift(driverId, success, failure) {
        RNHyperTrack.startShift(driverId, success, failure);
    },

    // Method to end shift in driver SDK
    endShift(success, failure) {
        RNHyperTrack.endShift(success, failure);
    }
}
