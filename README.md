# irrigation-system

## Description
The irrigation action is automated and controlled by the Mobile application where the user defines on which day and time will irrigation start. Also, the user can decide for how long will irrigation action stay active. After the action is done, the mobile application will schedule next irrigation based on user preferences.

## Features
* CRUD plan (Plan allows user to express his preferences when and for how long to irrigate surface)
* 9-hour forecast for chosen city (OpenWeatherMap API used for this)
* Surface irrigation (action triggered based on user preferences through currently active plan)
* Fetch readings from sensors and display them
* Set boundaries to sensor value so user can know if the value is above or below boundaries

## Tools and libraries
### Android
* MVVM architecture
* Navigation component
* Data binding
* Room
* Kotlin Coroutines
* Retrofit
* OkHttp
* Moshi
* Glide
* Material design
### Irrigation execution part
* ESP8266
* WebSocket server
* LiquidCrysal (F Malpartida)
* Adafruit Unified Sensor
* ArduinoJson (Benoit Blanchon)
* DHT sensor library
* WebSockets (Markus Sattler)

## Showcase

https://user-images.githubusercontent.com/44927926/120246302-1b97f000-c270-11eb-9bac-638ba5d12b49.mp4

https://user-images.githubusercontent.com/44927926/120243572-3f573800-c268-11eb-9ede-6665f8ddf849.mp4
