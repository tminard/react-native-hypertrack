
import { NativeModules } from 'react-native';

const { RNHyperTrack } = NativeModules;

module.exports = {
	showToast(message: string, duration: double = 2000) {
		RNHyperTrack.show(message, duration);
	},
	testMessage() {
		return "React Native HyperTrack SDK!";
	},
    startTrip(name, location) {
        RNHyperTrack.startTrip(name, location);
    }
	completeTask(taskId, callback) {
		RNHyperTrack.completeTask(taskId, callback);
	}
}