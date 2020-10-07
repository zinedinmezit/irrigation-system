#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <WebSocketsServer.h>

const char *ssid = "Tenda_29DC88";
const char *password = "alifakovac1";

const char *option = "ON";

ESP8266WebServer server(80);
WebSocketsServer webSocket(81);

void ConnectToWiFi(const char *ssid, const char *password);
void handleRoot();
void handleNotFound();

int relayPin = 5;

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
    webSocket.sendTXT(num, "Connected");
    break;
  }
  case WStype_TEXT:
  {

    const char *payloadText = (const char *)payload;
    if (strcmp(payloadText, option) == 0)
    {
      digitalWrite(relayPin, LOW);
      delay(5000L);
      digitalWrite(relayPin, HIGH);
    }
    break;
  }
  case WStype_ERROR:
  Serial.println("Error happened");
  break;

  default:
  Serial.println("Nothing happened");
  }
}

void setup()
{
  Serial.begin(9600);
  delay(10);
  Serial.println("\n");
  pinMode(relayPin, OUTPUT);
  digitalWrite(relayPin,HIGH);
  ConnectToWiFi(ssid, password);

  server.on("/", handleRoot);
  server.onNotFound(handleNotFound);

  server.begin();
  webSocket.begin();
  webSocket.onEvent(webSocketEvent);
}

void loop()
{
  webSocket.loop();
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
  // digitalWrite(relayPin, HIGH);
  server.send(200, "text/plain", "OKEY");
}

void handleNotFound()
{
  server.send(404, "text/plain", "Ne radi :(");
}
