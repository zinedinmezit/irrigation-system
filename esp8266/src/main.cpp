#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <WebSocketsServer.h>
#include <LiquidCrystal_I2C.h>
#include <Wire.h>
#include <DHT.h>

#define DHTTYPE DHT11

const char *ssid = "Tenda_29DC88";
const char *password = "alifakovac1";

const char *option = "ON";

int relayPin = 12;
int roomTemperaturePin = 14;

unsigned long previousMillis = 0;
const long interval = 2000;
int hummidityPin = A0;
int value = 0;

DHT dht(roomTemperaturePin, DHTTYPE);
LiquidCrystal_I2C lcd(0x27, 2, 1, 0, 4, 5, 6, 7, 3, POSITIVE);

WebSocketsServer webSocket(81);

void ConnectToWiFi(const char *ssid, const char *password);

void webSocketEvent(uint8_t num, WStype_t type, uint8_t *payload, size_t lenght)
{
  switch (type)
  {
  case WStype_DISCONNECTED:
    Serial.printf("[%u] Disconnected\n", num);
    break;
  case WStype_CONNECTED:
  {
    Serial.printf("[%u] Connected\n", num);
    break;
  }
  case WStype_TEXT:
  {

    const char *payloadText = (const char *)payload;
    if (strcmp(payloadText, option) == 0)
    {
      digitalWrite(relayPin, LOW);
      delay(2500);
      digitalWrite(relayPin, HIGH);
      Serial.println(payloadText);
    }
    else
    {
      Serial.println(payloadText);
    }

    break;
  }
  case WStype_ERROR:
    Serial.println("Error happened");
    break;
  }
}

void setup()
{
  Serial.begin(9600);

  delay(10);
  Serial.println("\n");
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
    value = analogRead(hummidityPin);
    String result = (String)(100 - ((value / 1024.00) * 100));

    float h = dht.readHumidity();
    // Read temperature as Celsius (the default)
    float t = dht.readTemperature();

    if (isnan(h) || isnan(t))
    {
      Serial.println(F("Failed to read from DHT sensor!"));
    }
    else
    {
      // Compute heat index in Celsius (isFahreheit = false)
      float hic = dht.computeHeatIndex(t, h, false);
      Serial.print(F("Humidity: "));
      Serial.print(h);
      Serial.print(F("%  Temperature: "));
      Serial.print(hic);
      Serial.print(F("°C "));
    }

    Serial.println("-------------------------------------------------");
    Serial.println(result);
    webSocket.sendTXT(0, result);
  }
}

void ConnectToWiFi(const char *ssid, const char *password)
{
  WiFi.begin(ssid, password);
  Serial.print("Connecting to ");
  Serial.print(ssid);

  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(1000);
  }

  String localIP = WiFi.localIP().toString();
  Serial.println("\n");
  Serial.println("Connected");
  lcd.print(localIP);
}
