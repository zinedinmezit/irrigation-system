#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>

const char *ssid = "Tenda_29DC88";
const char *password = "alifakovac1";

ESP8266WebServer server(80);

void ConnectToWiFi(const char *ssid, const char *password);
void handleRoot();
void handleNotFound();
void turnRelay();

int relayPin = 5;

void setup()
{
  Serial.begin(9600);
  delay(10);
  Serial.println("\n");

  ConnectToWiFi(ssid, password);

  server.on("/", handleRoot);
  server.on("/relay", turnRelay);
  server.onNotFound(handleNotFound);

  server.begin();

  pinMode(relayPin, OUTPUT);
}

void loop()
{
  server.handleClient();
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

void handleRoot()
{
  digitalWrite(relayPin, HIGH);
}

void handleNotFound()
{
  server.send(404, "text/plain", "Ne radi :(");
}

void turnRelay()
{
  digitalWrite(relayPin, LOW);
}