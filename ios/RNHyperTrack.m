
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
    tripParams.driverID = driverId;
    tripParams.taskIDs = taskIds;

    [[HTTransmitterClient sharedClient] startTripWithTripParams:tripParams completion:^(HTResponse <HTTrip *> * _Nullable response, NSError * _Nullable error) {
        if (error) {
            // Handle error and try again.
            NSDictionary *failure = @{@"error" : error.localizedDescription};
            failureCallback(@[failure]);
        } else {
            // If there is no error, use the tripID received in the callback in your app.
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
            successCallback(@[]);
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
            successCallback(@[]);
        }
    }];
}

@end
