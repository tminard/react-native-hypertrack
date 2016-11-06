
#import "RNHyperTrack.h"
#import "RCTLog.h"
#import <HTTransmitter/HTTransmitter.h>

@implementation RNHyperTrack

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(startTrip:(NSString *)name location:(NSString *)location)
{
  RCTLogInfo(@"Pretending to create an event %@ at %@", name, location);

  HTTripParams* tripParams = [[HTTripParams alloc] init];
}

@end
