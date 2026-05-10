# Dia 4 - Temperatura controlando LED RGB

## Objetivo

Integrar o sensor de temperatura DS18B20 com o LED RGB HW-479 usando ESP32.

O sistema agora consegue:
- ler temperatura em tempo real
- processar os dados
- responder visualmente usando cores

---

## Componentes utilizados

- ESP32 DevKit V1
- Sensor DS18B20
- LED RGB HW-479
- Protoboard
- Jumpers
- Arduino IDE

---

## Estrutura do sistema

### Sensor de temperatura

| DS18B20 | ESP32 |
|---|---|
| + | 3V3 |
| OUT | GPIO4 |
| - | GND |

---

### LED RGB

| HW-479 | ESP32 |
|---|---|
| R | GPIO23 |
| G | GPIO21 |
| B | GPIO19 |
| - | GND |

---

## Funcionamento

### Temperatura baixa
🔵 LED azul

### Temperatura média
🟢 LED verde

### Temperatura alta
🔴 LED vermelho

---

## Observações importantes

O módulo HW-479 apresentou comportamento invertido.

Descoberto durante os testes:

- HIGH = LED ligado
- LOW = LED desligado

Esse comportamento gerou várias etapas de debug e aprendizado sobre GPIO e lógica elétrica.

---

## Código utilizado

```cpp
#include <OneWire.h>
#include <DallasTemperature.h>

// SENSOR DE TEMPERATURA
#define ONE_WIRE_BUS 4

OneWire oneWire(ONE_WIRE_BUS);

DallasTemperature sensors(&oneWire);

// LED RGB
#define RED_PIN   23
#define GREEN_PIN 21
#define BLUE_PIN  19

void apagarTudo() {

  digitalWrite(RED_PIN, LOW);
  digitalWrite(GREEN_PIN, LOW);
  digitalWrite(BLUE_PIN, LOW);

}

void setup() {

  Serial.begin(115200);

  sensors.begin();

  pinMode(RED_PIN, OUTPUT);
  pinMode(GREEN_PIN, OUTPUT);
  pinMode(BLUE_PIN, OUTPUT);

  apagarTudo();

  Serial.println("Sistema iniciado!");

}

void loop() {

  sensors.requestTemperatures();

  float temperatura = sensors.getTempCByIndex(0);

  Serial.print("Temperatura: ");
  Serial.print(temperatura);
  Serial.println(" C");

  apagarTudo();

  // FRIO = AZUL
  if (temperatura < 28) {

    digitalWrite(BLUE_PIN, HIGH);

  }

  // NORMAL = VERDE
  else if (temperatura >= 28 && temperatura < 30) {

    digitalWrite(GREEN_PIN, HIGH);

  }

  // QUENTE = VERMELHO
  else {

    digitalWrite(RED_PIN, HIGH);

  }

  delay(1000);

}




Aprendizados
GPIO da ESP32
lógica HIGH e LOW
controle de LED RGB
sensores digitais
comunicação OneWire
organização de protoboard
debug eletrônico
integração hardware + software


Próximo passo

Integrar o sensor cardíaco MAX30102 para transformar sinais biológicos em resposta visual.
