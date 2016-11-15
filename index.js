
import { NativeModules } from 'react-native';

const { RNHyperTrack } = NativeModules;

module.exports = {
    // Method to intialize driver SDK with token(Publishable Key)
    initialize(token) {
        RNHyperTrack.initialize(token);
    },

    // connect SDK to the backend for a driver id
    connectDriver(driverId) {
        RNHyperTrack.connectDriver(driverId);
    },

    // return active driver id
    getActiveDriver(callback) {
        return RNHyperTrack.getActiveDriver(callback);
    },

    // check if transmitting locations
    isTransmitting(callback) {
        return RNHyperTrack.isTransmitting(callback);
    },

    // Method to start trip in driver SDK
    startTrip(driverId, taskIds, success, failure) {
        RNHyperTrack.startTrip(driverId, taskIds, success, failure);
    },

    // Method to complete task in driver SDK
    completeTask(taskId, success, failure) {
        RNHyperTrack.completeTask(taskId, success, failure);
    },

    // Method to end task in driver SDK
    endTrip(tripId, success, failure) {
        RNHyperTrack.endTrip(tripId, success, failure);
    },

    // Method to detect if location is active
    isActive() {
        return RNHyperTrack.isActive();
    },
}
