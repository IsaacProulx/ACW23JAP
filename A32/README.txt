

Closing the server while clients are still connected can cause issues.

There are some issues when running client and server on different networks:
When downloading on a client that is on a different network than the server, the client might hang (and potentially cause the server to crash).
Downloading seems to work fine if both the client and server are on the same network.
Uploading seems to work fine regardless of the network.

For testing with different networks, I just tunnelled the port using ngrok. So, it may act differently for you.