from MCP3008 import MCP3008
from time import sleep
import paho.mqtt.client as mqtt

isFlashlightOn = False

def on_connect(client, userdata, flags, rc):
    print(f"Connected with result code {rc}")

f = open("app.config", "r")
line = f.read()
words = line.split(';')

if (len(words) != 2):
    raise Exception("You have to provide an username and a password for the broker!")

username = words[0]
password = words[1].rstrip()

client = mqtt.Client()
client.on_connect = on_connect
client.username_pw_set(username=username, password=password)
client.connect("localhost")

adc = MCP3008()
while(True):
    value = adc.read(channel=0)
    print(value)
    if(value >=500 and isFlashlightOn==False):
        isFlashlightOn = True
        client.publish('raspberry/flashlight', payload=isFlashlightOn, qos=0, retain=False)
    else:
        if(value <500 and isFlashlightOn==True):
            isFlashlightOn = False
            client.publish('raspberry/flashlight', payload=isFlashlightOn, qos=0, retain=False)
    sleep(0.5)
    
client.loop_forever()