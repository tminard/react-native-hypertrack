
package com.reactlibrary;

import android.widget.Toast;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;


import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;


import io.hypertrack.lib.common.HyperTrack;
import io.hypertrack.lib.transmitter.service.HTTransmitterService;
import io.hypertrack.lib.transmitter.model.HTTrip;
import io.hypertrack.lib.transmitter.model.HTTripParams;
import io.hypertrack.lib.transmitter.model.HTTripParamsBuilder;
import io.hypertrack.lib.transmitter.model.callback.HTTripStatusCallback;
import io.hypertrack.lib.transmitter.model.callback.HTCompleteTaskStatusCallback;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class RNHyperTrackModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";

  public RNHyperTrackModule(ReactApplicationContext reactContext) {
      super(reactContext);
      this.reactContext = reactContext;
  }

  @Override
  public String getName() {
      return "RNHyperTrack";
  }

  @Override
  public Map<String, Object> getConstants() {
      final Map<String, Object> constants = new HashMap<>();
      constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
      constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
      return constants;
  }

  @ReactMethod
  public void initialize(String publishableKey) {
      HyperTrack.setPublishableApiKey(publishableKey, getReactApplicationContext());
      HTTransmitterService.initHTTransmitter(getReactApplicationContext());
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
  public String getActiveDriver() {
      Context context = getReactApplicationContext();
      HTTransmitterService transmitterService = HTTransmitterService.getInstance(context);
      return transmitterService.getActiveDriverID();
  }

  @ReactMethod
  public boolean isTransmitting() {
      Context context = getReactApplicationContext();
      HTTransmitterService transmitterService = HTTransmitterService.getInstance(context);
      return transmitterService.isDriverLive();
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
}
