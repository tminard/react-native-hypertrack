
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

import io.hypertrack.lib.common.HyperTrack;
import io.hypertrack.lib.transmitter.service.HTTransmitterService;
import io.hypertrack.lib.transmitter.model.HTTrip;
import io.hypertrack.lib.transmitter.model.HTTripParams;
import io.hypertrack.lib.transmitter.model.HTTripParamsBuilder;
import io.hypertrack.lib.transmitter.model.HTShift;
import io.hypertrack.lib.transmitter.model.HTShiftParams;
import io.hypertrack.lib.transmitter.model.HTShiftParamsBuilder;
import io.hypertrack.lib.transmitter.model.TransmitterConstants;
import io.hypertrack.lib.transmitter.model.callback.HTShiftStatusCallback;
import io.hypertrack.lib.transmitter.model.callback.HTTripStatusCallback;
import io.hypertrack.lib.transmitter.model.callback.HTEndAllTripsCallback;
import io.hypertrack.lib.transmitter.model.callback.HTCompleteTaskStatusCallback;

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
        filter.addAction(TransmitterConstants.HT_DRIVER_CURRENT_LOCATION_INTENT);
        filter.addAction(TransmitterConstants.HT_ON_LOCATION_SERVICE_STARTED_INTENT);
        filter.addAction(TransmitterConstants.HT_ON_DRIVER_NOT_ACTIVE_INTENT);
        LocalBroadcastManager.getInstance(getReactApplicationContext()).registerReceiver(mStatusBroadcastReceiver, filter);
    }

    @Override
    public String getName() {
        return "RNHyperTrack";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return constants;
    }

    @ReactMethod
    public void initialize(String publishableKey) {
        HyperTrack.setPublishableApiKey(publishableKey, getReactApplicationContext());
        HTTransmitterService.initHTTransmitter(getReactApplicationContext());
    }

    @ReactMethod
    public void getPublishableKey(final Callback callback) {
        Context context = getReactApplicationContext();
        callback.invoke(HyperTrack.getPublishableKey(context));
    }

    @ReactMethod
    public void show(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), message, duration).show();
    }

    @ReactMethod
    public void connectDriver(String driverID) {
        HTTransmitterService.connectDriver(getReactApplicationContext(), driverID);
    }

    @ReactMethod
    public void getConnectedDriver(final Callback callback) {
        Context context = getReactApplicationContext();
        HTTransmitterService transmitterService = HTTransmitterService.getInstance(context);
        callback.invoke(transmitterService.getDriverID());
    }

    @ReactMethod
    public void isTransmitting(final Callback callback) {
        Context context = getReactApplicationContext();
        HTTransmitterService transmitterService = HTTransmitterService.getInstance(context);
        callback.invoke(transmitterService.isDriverLive());
    }

    @ReactMethod
    public void startTrip(String driverID, ReadableArray taskIDList, final Callback successCallback, final Callback failureCallback) {
        Context context = getReactApplicationContext();
        HTTransmitterService transmitterService = HTTransmitterService.getInstance(context);

        ArrayList<String> taskIDs= this.toArrayList(taskIDList);

        HTTripParams htTripParams = new HTTripParamsBuilder().setDriverID(driverID)
                .setTaskIDs(taskIDs)
                .setOrderedTasks(false)
                .setIsAutoEnded(false)
                .createHTTripParams();


        transmitterService.startTrip(htTripParams, new HTTripStatusCallback() {
            @Override
            public void onSuccess(boolean isOffline, HTTrip htTrip) {
                try {
                    Gson gson = new Gson();
                    String tripJson = gson.toJson(htTrip);

                    WritableMap result = Arguments.createMap();
                    result.putBoolean("is_offline", isOffline);
                    result.putString("trip", tripJson);

                    successCallback.invoke(result);
                } catch (Exception e) {
                    WritableMap result = Arguments.createMap();
                    result.putString("error", e.toString());

                    failureCallback.invoke(result);
                }
            }

            @Override
            public void onError(Exception error) {
                try {
                    WritableMap result = Arguments.createMap();
                    if (error == null) {
                        result.putString("error", "");
                    } else {
                        result.putString("error", error.toString());
                    }

                    failureCallback.invoke(result);
                } catch (Exception e) {
                    WritableMap result = Arguments.createMap();
                    result.putString("error", e.toString());

                    failureCallback.invoke(result);
                }
            }
        });
    }

    @ReactMethod
    public void endTrip(String tripID, final Callback successCallback, final Callback failureCallback) {
        Context context = getReactApplicationContext();
        HTTransmitterService transmitterService = HTTransmitterService.getInstance(context);

        transmitterService.endTrip(tripID, new HTTripStatusCallback() {
            @Override
            public void onSuccess(boolean isOffline, HTTrip htTrip) {
                try {
                    Gson gson = new Gson();
                    String tripJson = gson.toJson(htTrip);

                    WritableMap result = Arguments.createMap();
                    result.putBoolean("is_offline", isOffline);
                    result.putString("trip", tripJson);
                    successCallback.invoke(result);
                } catch (Exception e) {
                    WritableMap result = Arguments.createMap();
                    result.putString("error", e.toString());

                    failureCallback.invoke(result);
                }
            }

            @Override
            public void onError(Exception e) {
                try {
                    WritableMap result = Arguments.createMap();
                    if (e == null) {
                        result.putString("error", "");
                    } else {
                        result.putString("error", e.toString());
                    }
                    failureCallback.invoke(result);
                } catch (Exception exception) {
                    WritableMap result = Arguments.createMap();
                    result.putString("error", exception.toString());

                    failureCallback.invoke(result);
                }
            }
        });
    }

    @ReactMethod
    public void endAllTrips(String driverID, final Callback successCallback, final Callback failureCallback) {
        Context context = getReactApplicationContext();
        HTTransmitterService transmitterService = HTTransmitterService.getInstance(context);

        transmitterService.endAllTrips(driverID, new HTEndAllTripsCallback() {
            @Override
            public void onSuccess() {
                try {
                    WritableMap result = Arguments.createMap();
                    successCallback.invoke(result);
                } catch (Exception e) {
                    WritableMap result = Arguments.createMap();
                    result.putString("error", e.toString());
                    failureCallback.invoke(result);
                }
            }

            @Override
            public void onError(Exception e) {
                try {
                    WritableMap result = Arguments.createMap();
                    if (e == null) {
                        result.putString("error", "");
                    } else {
                        result.putString("error", e.toString());
                    }
                    failureCallback.invoke(result);
                } catch (Exception exception) {
                    WritableMap result = Arguments.createMap();
                    result.putString("error", exception.toString());
                    failureCallback.invoke(result);
                }
            }
        });
    }

    @ReactMethod
    public void startShift(String driverID, final Callback successCallback, final Callback failureCallback) {
        Context context = getReactApplicationContext();
        HTTransmitterService transmitterService = HTTransmitterService.getInstance(context);

        HTShiftParamsBuilder htShiftParamsBuilder = new HTShiftParamsBuilder();
        HTShiftParams htShiftParams = htShiftParamsBuilder.setDriverID(driverID).createHTShiftParams();

        transmitterService.startShift(htShiftParams, new HTShiftStatusCallback() {
            @Override
            public void onSuccess(HTShift htShift) {
                try {
                    Gson gson = new Gson();
                    String shiftJson = gson.toJson(htShift);

                    WritableMap result = Arguments.createMap();
                    result.putString("trip", shiftJson);

                    successCallback.invoke(result);
                } catch (Exception e) {
                    WritableMap result = Arguments.createMap();
                    result.putString("error", e.toString());

                    failureCallback.invoke(result);
                }
            }

            @Override
            public void onError(Exception error) {
                try {
                    WritableMap result = Arguments.createMap();
                    if (error == null) {
                        result.putString("error", "");
                    } else {
                        result.putString("error", error.toString());
                    }

                    failureCallback.invoke(result);
                } catch (Exception exception) {
                    WritableMap result = Arguments.createMap();
                    result.putString("error", exception.toString());

                    failureCallback.invoke(result);
                }
            }
        });
    }

    @ReactMethod
    public void endShift(final Callback successCallback, final Callback failureCallback) {
        Context context = getReactApplicationContext();
        HTTransmitterService transmitterService = HTTransmitterService.getInstance(context);

        transmitterService.endShift(new HTShiftStatusCallback() {
            @Override
            public void onSuccess(HTShift htShift) {
                try {
                    Gson gson = new Gson();
                    String shiftJson = gson.toJson(htShift);
                    WritableMap result = Arguments.createMap();
                    result.putString("shift", shiftJson);
                    successCallback.invoke(result);
                } catch (Exception e) {
                    WritableMap result = Arguments.createMap();
                    result.putString("error", e.toString());
                    failureCallback.invoke(result);
                }
            }

            @Override
            public void onError(Exception e) {
                try {
                    WritableMap result = Arguments.createMap();
                    if (e == null) {
                        result.putString("error", "");
                    } else {
                        result.putString("error", e.toString());
                    }
                    failureCallback.invoke(result);
                } catch (Exception exception) {
                    WritableMap result = Arguments.createMap();
                    result.putString("error", exception.toString());

                    failureCallback.invoke(result);
                }
            }
        });
    }

    @ReactMethod
    public void completeTask(String taskID, final Callback successCallback, final Callback failureCallback) {
        Context context = getReactApplicationContext();
        HTTransmitterService transmitterService = HTTransmitterService.getInstance(context);

        transmitterService.completeTask(taskID, new HTCompleteTaskStatusCallback() {
            @Override
            public void onSuccess(String taskID) {
                try {
                    WritableMap result = Arguments.createMap();
                    result.putString("task_id", taskID);

                    successCallback.invoke(result);
                } catch (Exception e) {
                    WritableMap result = Arguments.createMap();
                    result.putString("error", e.toString());

                    successCallback.invoke(result);
                }
            }

            @Override
            public void onError(Exception e) {
                try {
                    WritableMap result = Arguments.createMap();
                    if (e == null) {
                        result.putString("error", "");
                    } else {
                        result.putString("error", e.toString());
                    }

                    failureCallback.invoke(result);
                } catch (Exception exception) {
                    WritableMap result = Arguments.createMap();
                    result.putString("error", exception.toString());

                    failureCallback.invoke(result);
                }
            }
        });
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
            if (paramIntent.getAction().equals(TransmitterConstants.HT_DRIVER_CURRENT_LOCATION_INTENT)) {
                Bundle bundle = paramIntent.getExtras();
                Location location = bundle.getParcelable(TransmitterConstants.HT_DRIVER_CURRENT_LOCATION_KEY);
                RNHyperTrackModule.this.sendCurrentLocation(location);
            }

            if (paramIntent.getAction().equals(TransmitterConstants.HT_ON_LOCATION_SERVICE_STARTED_INTENT)) {
                RNHyperTrackModule.this.sendActiveIntent();
            }

            if (paramIntent.getAction().equals(TransmitterConstants.HT_ON_DRIVER_NOT_ACTIVE_INTENT)) {
                RNHyperTrackModule.this.sendInactiveIntent();
            }
        }
    }
}
