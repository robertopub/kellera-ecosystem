# Dia 5 - ESP32 transmitindo dados biométricos em tempo real para Android via Bluetooth

---

# Objetivo

Criar uma comunicação em tempo real entre ESP32 e Android utilizando Bluetooth clássico, permitindo que sinais biométricos sejam transmitidos e visualizados diretamente na interface do aplicativo Kellera.

---

# O sistema agora consegue

- detectar o ESP32 automaticamente
- conectar via Bluetooth
- transmitir temperatura em tempo real
- atualizar interface Android dinamicamente
- representar sinais biológicos visualmente
- manter comunicação contínua ESP32 ↔ Android

---

# Componentes utilizados

- ESP32 DevKit V1
- Sensor DS18B20
- Smartphone Android
- Android Studio
- Arduino IDE
- Bluetooth Classic (SerialBT)
- Kotlin

---

# Arquitetura do sistema

DS18B20
↓
ESP32
↓ Bluetooth
Android (Kellera)
↓
Interface biométrica em tempo real

---

# Funcionamento

O ESP32 realiza a leitura contínua da temperatura utilizando o sensor DS18B20.

Os dados são transmitidos via Bluetooth clássico utilizando SerialBT.

O aplicativo Kellera detecta automaticamente o ESP32 emparelhado, estabelece conexão e recebe os dados em tempo real.

A interface Android atualiza dinamicamente os sinais biométricos sem necessidade de reinicialização.

---

# Problemas enfrentados

Durante a implementação ocorreram diversos desafios importantes:

- conflitos entre Activity e AppCompatActivity
- erros de permissões Bluetooth no Android
- problemas de reconexão contínua
- leitura incorreta do buffer Bluetooth
- atualização intermitente da interface
- necessidade de filtrar mensagens recebidas
- gerenciamento de comunicação em threads

---

# Aprendizados

- comunicação Bluetooth ESP32 ↔ Android
- uso de SerialBT
- manipulação de threads no Android
- leitura de InputStream
- atualização dinâmica de TextView
- tratamento de buffer serial
- integração hardware + mobile
- arquitetura de sistemas biométricos
- transmissão de dados em tempo real

---

# Conceito do projeto

Kellera não é apenas um aplicativo.

O projeto evolui como uma interface biométrica em tempo real entre sinais biológicos humanos e sistemas computacionais.

A proposta é transformar dados fisiológicos em uma camada visual inteligente, acessível e interpretável em tempo real.

---
# Imagens

![Kellera](../images/kellera-app-live.jpg)

![ESP32](../images/esp32-connected.jpg)

![ESP32](../images/arduino-serialbt.jpg)

---

# Próximo passo

Integração do sensor cardíaco MAX30102 para leitura real de batimentos cardíacos e evolução da interface biométrica dinâmica.
