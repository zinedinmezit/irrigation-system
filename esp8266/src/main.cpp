#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <WebSocketsServer.h>
#include <LiquidCrystal_I2C.h>
#include <Wire.h>
#include <DHT.h>
#include <ArduinoJson.h>

#define DHTTYPE DHT11

const char *ssid = "Nokia";
const char *password = "lozinka1";

void DisplayMessageLCD(String v1, String v2);
void SlideShow(String ip, String t, String h);
void ConnectToWiFi(const char *ssid, const char *password);
void WateringDelay(const long interval);
void sendSensorData(float hummidity, float temperature, String moisture);

//Watering intervals
const char *waterOption1 = "2000";
const char *waterOption2 = "5000";
const char *waterOption3 = "10000";

int relayPin = 12;
int dht11Pin = 14;

unsigned int slideShowOrder = 0;

unsigned long previousMillis = 0;
const long interval = 2000;
int moisturePin = A0;
int moistureAnalogValue = 0;

DHT dht(dht11Pin, DHTTYPE);
LiquidCrystal_I2C lcd(0x27, 2, 1, 0, 4, 5, 6, 7, 3, POSITIVE);

StaticJsonDocument<96> sensorValues;

WebSocketsServer webSocket(81);

void webSocketEvent(uint8_t num, WStype_t type, uint8_t *payload, size_t lenght)
{
  switch (type)
  {

  case WStype_CONNECTED:
  {
    Serial.printf("[%u] Connected", num);
    break;
  }

  case WStype_DISCONNECTED:
  {
    Serial.printf("[%u] Disconnected", num);
    break;
  }

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
  {
    Serial.println("Error");
    break;
  }
  }
}

void setup()
{

  Serial.begin(9600);
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

    SlideShow(WiFi.localIP().toString(), (String)t, (String)h);

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
    sensorValues["moisture"] = "NaN";
  else
  {
    sensorValues["moisture"] = moisture;
  }

  if (isnan(hummidity) || isnan(temperature))
  {
    sensorValues["dhtTemp"] = "NaN";
    sensorValues["dhtHumm"] = "NaN";
  }
  else
  {
    sensorValues["moisture"] = moisture;
    sensorValues["dhtTemp"] = (String)temperature;
    sensorValues["dhtHumm"] = (String)hummidity;
  }
  Serial.println(temperature);
  String output;
  serializeJson(sensorValues, output);
  webSocket.sendTXT(0, output);
}

void DisplayMessageLCD(String v1, String v2)
{
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print(v1);
  lcd.setCursor(0, 1);
  lcd.print(v2);
}

void SlideShow(String ip, String t, String h)
{

  if (webSocket.clientIsConnected((uint8_t)0))
  {
    if (slideShowOrder == 3)
      slideShowOrder = 0;
    switch (slideShowOrder)
    {
    case 0:
    {
      DisplayMessageLCD("IP Address", ip);
      slideShowOrder++;
      break;
    }
    case 1:
    {
      DisplayMessageLCD("Temperature", t);
      slideShowOrder++;
      break;
    }
    case 2:
    {
      DisplayMessageLCD("Hummidity", h);
      slideShowOrder = 0;
      break;
    }
    }
  }
  else
  {
    if (slideShowOrder != 3)
    {
      DisplayMessageLCD("IP Address", ip);
      slideShowOrder = 3;
    }
  }
}
