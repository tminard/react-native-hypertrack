
import { NativeModules } from 'react-native';

const { RNHyperTrack } = NativeModules;

module.exports = {
	showToast(message: string, duration: double = 2000) {
		RNHyperTrack.show(message, duration);
	},
	testMessage() {
		return "React Native HyperTrack SDK!";
	},
	completeTask(taskId, callback) {
		RNHyperTrack.completeTask(taskId, callback);
	}
}