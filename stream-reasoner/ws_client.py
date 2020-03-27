#!/usr/bin/env python

import websocket
import time

try:
    import thread
except ImportError:
    import _thread as thread

runs = 100

def on_message(ws, message):
    print(message)

def on_error(ws, error):
    print(error)

def on_close(ws):
    print("### closed ###")

def on_open(ws):
    def run(*args):
        for i in range(runs):
            time.sleep(5)
            ws.send("Ping")
        time.sleep(1)
        ws.close()
        print("thread terminating...")

    thread.start_new_thread(run, ())


if __name__ == "__main__":

    websocket.enableTrace(True)

    url = "ws://localhost:8082"
    ws = websocket.WebSocketApp(url, on_message = on_message, on_error = on_error, on_close = on_close)

    ws.on_open = on_open
    ws.run_forever()
