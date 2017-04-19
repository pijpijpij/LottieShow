# LottieShow
This is a visualisation tool for [Lotties](http://airbnb.design/lottie/), animations created by Air BnB.

It lists the Lotties it knows about. Each one can be viewed in more detail.

## Integration
The app can import Lotties from a few sources:
- The local SD card,
- Google Drive,
- The `Downloads` folder. That provides basic e-mail integration: all files included in an e-mail are created in that 
folder.
 
These imports are achieved via the Storage Access framework.

# Further ideas

They are, in **no** particular order:
- Integration with [Dropbox](https://github.com/pijpijpij/LottieShow/issues/21)
- Opening a Lottie directly [from other applications](https://github.com/pijpijpij/LottieShow/issues/22). That's in essence declaring the app as a handler for *.json 
files.
- Monitor removal/insertion of External storage (see https://developer.android.com/reference/android/os/Environment.html#getExternalStorageDirectory())
- Online library
- Export as video
- ~~Show [versions](https://github.com/pijpijpij/LottieShow/issues/27) of Lottie and the library used.~~
- Change background colour (with a colour picker or a simple RGB value)
- auto-sync with Google drive or Dropbox
- export from Bodymovin
- resizing (during replay) with a slider.
- play animation:
  - ~~continuously~~
  - back and forth
- reverse slider
- show in context (put a screen grab in the background)


# License

    Copyright 2017 PiJ International

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

