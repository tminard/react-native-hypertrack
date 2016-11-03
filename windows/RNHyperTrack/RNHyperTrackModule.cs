using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Com.Reactlibrary.RNHyperTrack
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNHyperTrackModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNHyperTrackModule"/>.
        /// </summary>
        internal RNHyperTrackModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNHyperTrack";
            }
        }
    }
}
