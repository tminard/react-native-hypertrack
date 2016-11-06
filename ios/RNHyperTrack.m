
#import "RNHyperTrack.h"
#import <HTTransmitter/HTTransmitter.h>

@implementation RNHyperTrack

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(startTrip:(NSString *)driverId
                  :(NSArray *)taskIds
                  :(RCTResponseSenderBlock)successCallback
                  :(RCTResponseSenderBlock)failureCallback)
{
    HTTripParams* tripParams = [[HTTripParams alloc] init];
    tripParams.driverId = driverId;
    tripParams.tasksIDs = taskIds;

    [[HTTransmitterClient sharedClient] startTripWithTripParams:tripParams completion:^(HTTrip* trip, NSError* error) {
        if (error) {
            // Handle error and try again.
            failureCallback();
        } else {
            // If there is no error, use the tripID received in the callback in your app.
            successCallback();
        }
    }];
}

RCT_EXPORT_METHOD(completeTask:(NSString *)taskId
                  :(RCTResponseSenderBlock)successCallback
                  :(RCTResponseSenderBlock)failureCallback)
{
    // Mark task as completed by passing taskID
    [[HTTransmitterClient sharedClient] completeTaskWithTaskID:taskId completion:^(NSString* taskID, NSError* error) {

        if (error) {
            // Handle error and try again.
            failureCallback();
        } else {
            // If there is no error, use the taskID received in the callback in your app.
            successCallback();
        }
    }];
}

RCT_EXPORT_METHOD(endTrip:(NSString *)tripId
                  :(RCTResponseSenderBlock)successCallback
                  :(RCTResponseSenderBlock)failureCallback)
{
    // Warning: does not use the tripId

    [[HTTransmitterClient sharedClient] endTripWithCompletion:^(HTTrip* trip, NSError* error) {

        if (error) {
            // Handle error and try again.
            failureCallback();
        } else {
            // If there is no error, use the trip received in the callback in your app.
            successCallback();
        }
    }];
}

@end
