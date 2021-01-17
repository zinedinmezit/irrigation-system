#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <WebSocketsServer.h>
#include <LiquidCrystal_I2C.h>
#include <Wire.h>
#include <DHT.h>
#include <ArduinoJson.h>

#define DHTTYPE DHT11

//WiFi credentials
const char *ssid = "Tenda_29DC88";
const char *password = "alifakovac1";

//Watering intervals
const char *waterOption1 = "2000";
const char *waterOption2 = "5000";
const char *waterOption3 = "10000";

int relayPin = 12;
int dht11Pin = 14;

unsigned long previousMillis = 0;
const long interval = 2000;
int moisturePin = A0;
int moistureAnalogValue = 0;

DHT dht(dht11Pin, DHTTYPE);
LiquidCrystal_I2C lcd(0x27, 2, 1, 0, 4, 5, 6, 7, 3, POSITIVE);

StaticJsonDocument<96> sensorValues;

WebSocketsServer webSocket(81);

void ConnectToWiFi(const char *ssid, const char *password);
void WateringDelay(const long interval);
void sendSensorData(float hummidity, float temperature, String moisture);

void webSocketEvent(uint8_t num, WStype_t type, uint8_t *payload, size_t lenght)
{
  switch (type)
  {
  case WStype_DISCONNECTED:
    break;

  case WStype_CONNECTED:
    break;

  case WStype_TEXT:
  {
    const char *payloadText = (const char *)payload;
    if (strcmp(payloadText, waterOption1) == 0)
    {
      digitalWrite(relayPin, LOW);
      WateringDelay(2000);
      digitalWrite(relayPin, HIGH);
    }
    else if (strcmp(payloadText, waterOption2) == 0)
    {
      digitalWrite(relayPin, LOW);
      WateringDelay(5000);
      digitalWrite(relayPin, HIGH);
    }
    else if (strcmp(payloadText, waterOption3) == 0)
    {
      digitalWrite(relayPin, LOW);
      WateringDelay(10000);
      digitalWrite(relayPin, HIGH);
    }
    break;
  }

  case WStype_ERROR:
    break;
  }
}

void setup()
{

  dht.begin();

  lcd.begin(16, 2);
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("IP Address");
  lcd.setCursor(0, 1);

  pinMode(relayPin, OUTPUT);
  digitalWrite(relayPin, HIGH);

  ConnectToWiFi(ssid, password);

  webSocket.begin();
  webSocket.onEvent(webSocketEvent);
}

void loop()
{
  webSocket.loop();

  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval)
  {
    previousMillis = currentMillis;
    moistureAnalogValue = analogRead(moisturePin);
    String moistureValueString = (String)(100 - ((moistureAnalogValue / 1024.00) * 100));

    float h = dht.readHumidity();
    float t = dht.readTemperature();

    sendSensorData(h, t, moistureValueString);
  }
}

void ConnectToWiFi(const char *ssid, const char *password)
{
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED)
  {
    delay(1000);
  }

  String localIP = WiFi.localIP().toString();
  lcd.print(localIP);
}

void WateringDelay(const long interval)
{
  unsigned long trackMillis = 0;
  unsigned long currentMillis = millis();

  while (trackMillis < currentMillis + interval)
  {
    trackMillis = millis();
    yield();
  }
}

void sendSensorData(float hummidity, float temperature, String moisture)
{

  sensorValues.clear();

  if (moisture.isEmpty())
    sensorValues["error_moisture"] = "Can't read moisture";
  else
  {
    sensorValues["moisture"] = moisture;
    sensorValues["error_moisture"] = "";
  }

  if (isnan(hummidity) || isnan(temperature))
  {
    sensorValues["error_dht11"] = "Can't read dht11";
  }
  else
  {
    sensorValues["moisture"] = moisture;
    sensorValues["dhtTemp"] = (String)temperature;
    sensorValues["dhtHumm"] = (String)hummidity;
    sensorValues["error_dht11"] = "";
  }

  String output;
  serializeJson(sensorValues, output);
  webSocket.sendTXT(0, output);
}
