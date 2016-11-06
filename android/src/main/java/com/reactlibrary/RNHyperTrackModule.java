
package com.reactlibrary;

import android.widget.Toast;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;

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

    HyperTrack.setPublishableApiKey("pk_1507af78ef9dca2d250bdd6cf835e315bde4ad96", getReactApplicationContext());
    HTTransmitterService.initHTTransmitter(getReactApplicationContext());
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
  public void show(String message, int duration) {
    Toast.makeText(getReactApplicationContext(), message, duration).show();
  }

  @ReactMethod
  public void startTrip(String driverId, String taskId) {
    // TODO
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
                  JSONObject result = new JSONObject();
                  result.put("is_offline", isOffline);
                  result.put("trip", tripJson);
                  successCallback.invoke("");
              } catch (JSONException e) {
                  successCallback.invoke("");
              }
          }

          @Override
          public void onError(Exception e) {
              try {
                  JSONObject result = new JSONObject();
                  if (e == null) {
                      result.put("error", "");
                  } else {
                      result.put("error", e.toString());
                  }
                  failureCallback.invoke(e.toString());
              } catch (JSONException exception) {
                  failureCallback.invoke("");
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
                   JSONObject result = new JSONObject();
                   result.put("is_offline", isOffline);
                   result.put("trip", tripJson);
                   successCallback.invoke("");
               } catch (JSONException e) {
                   successCallback.invoke("");
               }
           }

           @Override
           public void onError(Exception e) {
               try {
                   JSONObject result = new JSONObject();
                   if (e == null) {
                       result.put("error", "");
                   } else {
                       result.put("error", e.toString());
                   }
                   failureCallback.invoke("");
               } catch (JSONException exception) {
                   failureCallback.invoke("");
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
                  JSONObject result = new JSONObject();
                  result.put("task_id", taskID);
                  successCallback.invoke("Success");
              } catch (JSONException e) {
                  successCallback.invoke("");
              }
          }

          @Override
          public void onError(Exception e) {
              try {
                  JSONObject result = new JSONObject();
                  if (e == null) {
                      result.put("error", "");
                  } else {
                      result.put("error", e.toString());
                  }
                  failureCallback.invoke(e.toString());
              } catch (JSONException exception) {
                  failureCallback.invoke("");
              }
          }
      });
  }

  public ArrayList<String> toArrayList(ReadableArray taskIDs) {
    ArrayList<String> arrayList = new ArrayList<>();
    for (int i = 0; i < taskIDs.size(); i++) {
      switch (taskIDs.getType(i)) {
        case Null:
          arrayList.add(null);
          break;
        case Boolean:
          throw new IllegalArgumentException("Could not convert object at index: " + i + ".");
        case Number:
          throw new IllegalArgumentException("Could not convert object at index: " + i + ".");
        case String:
          arrayList.add(taskIDs.getString(i));
          break;
        case Map:
          throw new IllegalArgumentException("Could not convert object at index: " + i + ".");
        case Array:
          throw new IllegalArgumentException("Could not convert object at index: " + i + ".");
        default:
          throw new IllegalArgumentException("Could not convert object at index: " + i + ".");
      }
    }
    return arrayList;
  }
}