
#import "RNHyperTrack.h"
#import <HTTransmitter/HTTransmitter.h>

@implementation RNHyperTrack

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

- (id)init
{
    NSLog(@"init for wrapper called");

    [[NSNotificationCenter defaultCenter] addObserverForName:HTLocationServiceDidTerminate object:nil queue:nil usingBlock:^(NSNotification * _Nonnull note) {
        NSLog(@"notification received for location service terminate : %@", note);
        [self.bridge.eventDispatcher sendAppEventWithName:@"driverIsInactive" body:nil];
    }];
}

- (void)dealloc {
    NSLog(@"dealloc for wrapper called");

    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(initialize:(NSString *)token)
{
    NSString * const HyperTrackPublishableKey = token;
    [HyperTrack setPublishableAPIKey:HyperTrackPublishableKey];
}

RCT_EXPORT_METHOD(getPublishableKey:(RCTResponseSenderBlock)callback)
{
    NSString *publishableKey = [HyperTrack publishableKey];

    if (publishableKey) {
        callback(@[publishableKey]);
    } else {
        callback(@[@""]);
    }
}

RCT_EXPORT_METHOD(connectDriver:(NSString *)driverID)
{
    [[HTTransmitterClient sharedClient] connectDriverWithDriverID:driverID completion:nil];
}

RCT_EXPORT_METHOD(getConnectedDriver:(RCTResponseSenderBlock)callback)
{
    NSString *driverID = [[HTTransmitterClient sharedClient] activeDriverID];

    if (driverID) {
        callback(@[driverID]);
    } else {
        callback(@[@""]);
    }
}

RCT_EXPORT_METHOD(isTransmitting:(RCTResponseSenderBlock)callback)
{
    callback(@[[NSNumber numberWithBool:[[HTTransmitterClient sharedClient] transmitingLocation]]]);
}

RCT_EXPORT_METHOD(startTrip:(NSString *)driverId
                  :(NSArray *)taskIds
                  :(RCTResponseSenderBlock)successCallback
                  :(RCTResponseSenderBlock)failureCallback)
{
    HTTripParams* tripParams = [[HTTripParams alloc] init];
    tripParams.driverID = driverId;
    tripParams.taskIDs = taskIds;

    [[HTTransmitterClient sharedClient] startTripWithTripParams:tripParams completion:^(HTResponse <HTTrip *> * _Nullable response, NSError * _Nullable error) {
        if (error) {
            // Handle error and try again.
            NSDictionary *failure = @{@"error" : error.localizedDescription};
            failureCallback(@[failure]);
        } else {
            // If there is no error, use the tripID received in the callback in your app.
            NSDictionary *success = @{@"trip" : response.result.dictionaryValue.jsonString};
            successCallback(@[success]);
        }
    }];
}

RCT_EXPORT_METHOD(completeTask:(NSString *)taskId
                  :(RCTResponseSenderBlock)successCallback
                  :(RCTResponseSenderBlock)failureCallback)
{
    // Mark task as completed by passing taskID
    [[HTTransmitterClient sharedClient] completeTaskWithTaskID:taskId completion:^(HTResponse <HTTask *> * _Nullable response, NSError * _Nullable error) {

        if (error) {
            // Handle error and try again.
            NSDictionary *failure = @{@"error" : error.localizedDescription};
            failureCallback(@[failure]);
        } else {
            // If there is no error, use the taskID received in the callback in your app.
            NSDictionary *success = @{@"task" : response.result.dictionaryValue.jsonString};
            successCallback(@[success]);
        }
    }];
}

RCT_EXPORT_METHOD(endTrip:(NSString *)tripId
                  :(RCTResponseSenderBlock)successCallback
                  :(RCTResponseSenderBlock)failureCallback)
{
    [[HTTransmitterClient sharedClient] endTripWithTripID:tripId completion:^(HTResponse <HTTrip *> * _Nullable response, NSError * _Nullable error) {

        if (error) {
            // Handle error and try again.
            NSDictionary *failure = @{@"error" : error.localizedDescription};
            failureCallback(@[failure]);
        } else {
            // If there is no error, use the trip received in the callback in your app.
            NSDictionary *success = @{@"trip" : response.result.dictionaryValue.jsonString};
            successCallback(@[success]);
        }
    }];
}

RCT_EXPORT_METHOD(endAllTrips:(NSString *)driverId
                  :(RCTResponseSenderBlock)successCallback
                  :(RCTResponseSenderBlock)failureCallback)
{
    [[HTTransmitterClient sharedClient] endAllTripsWithCompletion:^(NSError * _Nullable error) {

        if (error) {
            // Handle error and try again.
            NSDictionary *failure = @{@"error" : error.localizedDescription};
            failureCallback(@[failure]);
        } else {
            // If there is no error, use the trip received in the callback in your app.
            NSDictionary *success = @{};
            successCallback(@[success]);
        }
    }];
}

RCT_EXPORT_METHOD(startShift:(NSString *)driverId
                  :(RCTResponseSenderBlock)successCallback
                  :(RCTResponseSenderBlock)failureCallback)
{
    HTShiftParams *shiftParams = [[HTShiftParams alloc] init];
    shiftParams.driverID = driverId;

    [[HTTransmitterClient sharedClient] startShiftWithShiftParams:shiftParams completion:^(HTResponse * _Nullable response, NSError * _Nullable error) {
        if (error) {
            // Handle error and try again.
            NSDictionary *failure = @{@"error" : error.localizedDescription};
            failureCallback(@[failure]);
        } else {
            // If there is no error, use the trip received in the callback in your app.
            NSDictionary *success = @{@"shift" : @""};
            successCallback(@[success]);
        }
    }];
}

RCT_EXPORT_METHOD(endShift:(RCTResponseSenderBlock)successCallback
                  :(RCTResponseSenderBlock)failureCallback)
{
    [[HTTransmitterClient sharedClient] endShiftWithCompletion:^(HTResponse * _Nullable response, NSError * _Nullable error) {
        if (error) {
            // Handle error and try again.
            NSDictionary *failure = @{@"error" : error.localizedDescription};
            failureCallback(@[failure]);
        } else {
            // If there is no error, use the trip received in the callback in your app.
            NSDictionary *success = @{@"shift" : @""};
            successCallback(@[success]);
        }
    }];
}

@end
