
#import "RNHyperTrack.h"
#import <HTTransmitter/HTTransmitter.h>
#import "RCTBridge.h"
#import "RCTEventDispatcher.h"

@implementation RNHyperTrack

@synthesize bridge = _bridge;

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE();

-(id)init
{
    self = [super init];
    if ( self ) {
        NSLog(@"self is defined: %@", self);
    }
    NSLog(@"init for wrapper called");

    [[NSNotificationCenter defaultCenter] addObserverForName:HTOnLocationServiceStoppedNotification object:nil queue:nil usingBlock:^(NSNotification * _Nonnull note) {
        NSLog(@"notification received for location service terminate : %@", note);
        [self.bridge.eventDispatcher sendAppEventWithName:@"driverIsInactive" body:nil];
    }];

    [[NSNotificationCenter defaultCenter] addObserverForName:HTOnLocationServiceStartedNotification object:nil queue:nil usingBlock:^(NSNotification * _Nonnull note) {
        NSLog(@"notification received for location service start : %@", note);
        [self.bridge.eventDispatcher sendAppEventWithName:@"driverIsActive" body:nil];
    }];

    return self;
}

-(void)dealloc {
    NSLog(@"dealloc for wrapper called");

    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

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

RCT_EXPORT_METHOD(createUser:(NSString *)name
                  :(RCTResponseSenderBlock)callback)
{
    //
}

RCT_EXPORT_METHOD(setUserId:(NSString *)userId)
{
    //
}

RCT_EXPORT_METHOD(getUserId:(RCTResponseSenderBlock)callback)
{
    //
}

RCT_EXPORT_METHOD(startTracking:(RCTResponseSenderBlock)callback)
{
    //
}

RCT_EXPORT_METHOD(stopTracking:(RCTResponseSenderBlock)callback)
{
    //
}

RCT_EXPORT_METHOD(isTracking:(RCTResponseSenderBlock)callback)
{
    //
}

RCT_EXPORT_METHOD(completeAction:(NSString *)actionId
                  :(RCTResponseSenderBlock)callback)
{
    //
}

@end
