#!/usr/bin/env python

import asyncio
import websockets

async def echo(websocket, path):
    async for message in websocket:
        print("Received: " + message)
        await websocket.send(message)

start_server = websockets.serve(echo, "localhost", 8082)

asyncio.get_event_loop().run_until_complete(start_server)
asyncio.get_event_loop().run_forever()
