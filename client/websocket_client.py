from websocket import create_connection
import time

ws = create_connection("ws://localhost:34242/test")

while True:
    ws.send("a")
    print("sent a")
    time.sleep(5)

# ws.recv()
