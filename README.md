**Challenges app**
---
<img width="346" alt="Screenshot 2022-08-24 at 12 53 44" src="https://user-images.githubusercontent.com/38189560/186388946-f28a121d-3173-456e-b03c-5972f821e0e8.png">

When the user presses the add button - app shows random challenge from "http://www.boredapi.com/api/".
User can accept it or decline. If user accepts the challenge - challenge saved in DB. If user declines the challenge - the next one is fetched from API.
User can mark accepted challenges as completed.

It is just a sample app with network requests and local DB.



**Used architecture and libraries**
---

Clean architecture is chosen for the app, with MVVM in presentation layer.
Retrofit for network requests.
Coroutines.
Room as a local storage.
Hilt for DI.
