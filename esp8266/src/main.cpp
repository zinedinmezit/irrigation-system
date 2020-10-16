#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <WebSocketsServer.h>

const char *ssid = "Tenda_29DC88";
const char *password = "alifakovac1";

const char *option = "ON";
int relayPin = 5;


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
    webSocket.broadcastTXT("Conne.cted");
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

unsigned long previousMillis = 0;
const long interval = 2000;

int hummidityPin = A0;
int value = 0;

void setup()
{
  Serial.begin(9600);
  delay(10);
  Serial.println("\n");
  pinMode(relayPin, OUTPUT);
  digitalWrite(relayPin, HIGH);
  ConnectToWiFi(ssid, password);

  webSocket.begin();
  webSocket.onEvent(webSocketEvent);
}

void loop()
{
  webSocket.loop();
  value = analogRead(hummidityPin);

  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval)
  {
    previousMillis = currentMillis;
    double percentage = 100L * value / 1023;
    Serial.println(percentage);
//    webSocket.broadcastTXT(value);
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
  Serial.println(localIP);
}
