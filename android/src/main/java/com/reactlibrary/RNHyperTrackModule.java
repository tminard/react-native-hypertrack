
package com.reactlibrary;

import android.widget.Toast;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import java.util.Map;
import java.util.HashMap;

import io.hypertrack.lib.common.HyperTrack;
import io.hypertrack.lib.transmitter.service.HTTransmitterService;

public class RNHyperTrackModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";

  public RNHyperTrackModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;

    HyperTrack.setPublishableApiKey("ACCOUNT_PK", getReactApplicationContext());
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
  public void completeTask(String taskID, final Callback callback) {
      Context context = getReactApplicationContext();
      HTTransmitterService transmitterService = HTTransmitterService.getInstance(context);

      transmitterService.completeTask(taskID, new HTCompleteTaskStatusCallback() {
          @Override
          public void onSuccess(String taskID) {
              try {
                  JSONObject result = new JSONObject();
                  result.put("task_id", taskID);
                  callback.invoke("Success");
              } catch (JSONException e) {
                  callback.invoke("");
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
                  callback.invoke(e.toString());
              } catch (JSONException exception) {
                  callback.invoke("");
              }
          }
      });
  }
}