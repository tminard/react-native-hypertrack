
import { NativeModules } from 'react-native';

const { RNHyperTrack } = NativeModules;

module.exports = {
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
}