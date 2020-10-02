#include <Arduino.h>
#include <ESP8266WiFi.h>

const char *ssid = "Tenda_29DC88";
const char *password = "alifakovac1";

void ConnectToWiFi(const char *ssid, const char *password);

void setup()
{
  Serial.begin(9600);
  delay(10);
  Serial.println("\n");

  ConnectToWiFi(ssid, password);
}

void loop()
{
  // put your main code here, to run repeatedly:
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

  Serial.println("\n");
  Serial.println("Connected");
  Serial.println(WiFi.localIP());
}