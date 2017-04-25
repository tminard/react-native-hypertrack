import Foundation


@objc(RNHyperTrack)
class RNHyperTrack: NSObject {


    @objc func addEvent(_ name: String, location: String, date: NSNumber, callback: RCTResponseSenderBlock ) -> Void {
        // Date is ready to use!
        NSLog("%@ %@ %@", name, location, date)
        let ret:[String:Any] =  ["name": name, "location": location, "date" : date]
        callback([ret])
        self.sendEvent(withName: "EventReminder", body: ret)
    }

}
