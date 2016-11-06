
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
}

RCT_EXPORT_METHOD(completeTask:(NSString *)taskId
                              :(RCTResponseSenderBlock)successCallback
                              :(RCTResponseSenderBlock)failureCallback)
{
  // 
}

RCT_EXPORT_METHOD(endTrip:(NSString *)tripId
                         :(RCTResponseSenderBlock)successCallback
                         :(RCTResponseSenderBlock)failureCallback)
{
  //
}

@end
