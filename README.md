# Automated Night Light
This project turns the user's phone into an automatic night-light. The board uses a sensor, which detects the light levels in the room. When the sensor will detect that the room is too dark, the phone’s flashlight will turn on automatically, and vice-versa: when the room is light up again, the phone’s flashlight will turn off.

## Schematics

![Schematics](schematics.png)

## Pre-requisites
- Raspberry Pi 4B board
- Android Studio (I'm using Android Studio Artic Fox)
- The following individual components:
    - LDR module
    - jumper wires
    - 1 breadboard

## Setup and Build

To setup, follow these steps below.

1. Install Mosquitto MQTT Server on the Raspberry Pi
    ```
    sudo apt install mosquitto mosquitto-clients
    ```
2. Configure Mosquitto username and password
  - create the password file, which will contain the username and encrypted password
    ```
    sudo mosquitto_passwd -c /etc/mosquitto/passwd.txt <user_name>
    ```
  - add the following entries in the mosquitto.conf file, inside /etc/mosquitto folder
    ```
    allow_anonymous false
    password_file /etc/mosquitto/passwd.txt
    ```
  - restart the mosquitto server to make sure the changes are saved
    ```
    sudo systemctl restart mosquitto
    ```
3. Copy the LightSensorApp folder to Raspberry Pi
4. Modify the username and password in app.config file, inside the LightSensorApp folder
  - the username and password must be on the same line, separated by a semicolon: ';'
5. In the CompanionApp project, inside the build.gradle file from the app folder, modify the mosquitto username and password, and the Raspberry Pi server's IP address. The port must remain 1883

The `CompanionApp` project will run on the companion device e.g. Android phone.

## Running

To run the `ConsoleApp` project on your Android phone:
1. Deploy and run the `ConsoleApp` project
2. Verify that input is received from the broker
3. Verify that the flashlight is enabled when it's dark and disabled when there's light

To run the `LightSensorApp` module on a Raspberry Pi 4B board:

1. Run the following command from the terminal:
  ```
  python3 main.py
  ```
2. Verify that the sensor works and the program is printing the analog values
