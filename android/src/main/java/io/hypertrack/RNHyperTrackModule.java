
package io.hypertrack;

import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.LifecycleEventListener;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import com.hypertrack.lib.HyperTrack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class RNHyperTrackModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private final ReactApplicationContext reactContext;
    private final StatusBroadcastReceiver mStatusBroadcastReceiver;

    public RNHyperTrackModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.mStatusBroadcastReceiver = new StatusBroadcastReceiver();

        IntentFilter filter = new IntentFilter();
        // filter.addAction(TransmitterConstants.HT_DRIVER_CURRENT_LOCATION_INTENT);
        // filter.addAction(TransmitterConstants.HT_ON_LOCATION_SERVICE_STARTED_INTENT);
        // filter.addAction(TransmitterConstants.HT_ON_DRIVER_NOT_ACTIVE_INTENT);
        LocalBroadcastManager.getInstance(getReactApplicationContext()).registerReceiver(mStatusBroadcastReceiver, filter);
    }

    @Override
    public String getName() {
        return "RNHyperTrack";
    }

    @ReactMethod
    public void initialize(String publishableKey) {
        HyperTrack.initialize(getReactApplicationContext(), publishableKey);
    }

    @ReactMethod
    public void getPublishableKey(final Callback callback) {
        Context context = getReactApplicationContext();
        callback.invoke(HyperTrack.getPublishableKey(context));
    }

    @ReactMethod
    public void createUser(String name, final Callback callback) {
        // HTTransmitterService.connectDriver(getReactApplicationContext(), driverID);
    }

    @ReactMethod
    public void setUserId(String userId) {
        // HTTransmitterService.connectDriver(getReactApplicationContext(), driverID);
    }

    @ReactMethod
    public void getUserId(final Callback callback) {
        // HTTransmitterService.connectDriver(getReactApplicationContext(), driverID);
    }

    @ReactMethod
    public void startTracking(final Callback callback) {
        // HTTransmitterService.connectDriver(getReactApplicationContext(), driverID);
    }

    @ReactMethod
    public void stopTracking(final Callback callback) {
        // HTTransmitterService.connectDriver(getReactApplicationContext(), driverID);
    }

    @ReactMethod
    public void isTracking(final Callback callback) {
        // HTTransmitterService.connectDriver(getReactApplicationContext(), driverID);
    }

    @ReactMethod
    public void completeAction(String actionId, final Callback callback) {
        // HTTransmitterService.connectDriver(getReactApplicationContext(), driverID);
    }

    @Override
    public void onHostDestroy() {
        LocalBroadcastManager.getInstance(getReactApplicationContext()).unregisterReceiver(mStatusBroadcastReceiver);
    }

    @Override
    public void onHostPause() { }

    @Override
    public void onHostResume() { }

    private ArrayList<String> toArrayList(ReadableArray taskIDs) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < taskIDs.size(); i++) {
            switch (taskIDs.getType(i)) {
                case String:
                    arrayList.add(taskIDs.getString(i));
                    break;
                default:
                    throw new IllegalArgumentException("Task ID at index " + i + " should be String");
            }
        }
        return arrayList;
    }

    private void sendCurrentLocation(Location location) {
        WritableMap params = Arguments.createMap();
        params.putDouble("latitude", location.getLatitude());
        params.putDouble("longitude", location.getLongitude());
        sendEvent("currentLocationDidChange", params);
    }

    private void sendActiveIntent() {
        WritableMap params = Arguments.createMap();
        sendEvent("driverIsActive", params);
    }

    private void sendInactiveIntent() {
        WritableMap params = Arguments.createMap();
        sendEvent("driverIsInactive", params);
    }

    private void sendEvent(String eventName, WritableMap params) {
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    private class StatusBroadcastReceiver extends BroadcastReceiver {
        private StatusBroadcastReceiver() { }

        public void onReceive(Context paramContext, Intent paramIntent) {
            // if (paramIntent.getAction().equals(TransmitterConstants.HT_DRIVER_CURRENT_LOCATION_INTENT)) {
            //     Bundle bundle = paramIntent.getExtras();
            //     Location location = bundle.getParcelable(TransmitterConstants.HT_DRIVER_CURRENT_LOCATION_KEY);
            //     RNHyperTrackModule.this.sendCurrentLocation(location);
            // }
            //
            // if (paramIntent.getAction().equals(TransmitterConstants.HT_ON_LOCATION_SERVICE_STARTED_INTENT)) {
            //     RNHyperTrackModule.this.sendActiveIntent();
            // }
            //
            // if (paramIntent.getAction().equals(TransmitterConstants.HT_ON_DRIVER_NOT_ACTIVE_INTENT)) {
            //     RNHyperTrackModule.this.sendInactiveIntent();
            // }
        }
    }
}
