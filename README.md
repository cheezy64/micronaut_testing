## Test project for Micronaut WebSocket @OnClose handler
### The problem
`@OnClose` is not called for WebSockets in bad network environments with lots of dropped packets

https://github.com/micronaut-projects/micronaut-core/issues/8493

### Tools we use
clumsy - https://jagt.github.io/clumsy/ to make your network condition on Windows significantly worse in a controlled way

### Repro steps

```
1. Start the server.
2. Start the client.
3. Configure clumsy to drop 50% of the packets and use filter tcp and (tcp.DstPort == 34242 or tcp.SrcPort == 34242).
4. Press "Start" button on clumsy.
5. Kill the client (python).
```

### Actual Behavior
OnClose handler is NOT called when the python process is killed and the TCP Connection is destroyed.

### Expected behavior
OnClose handler would be called.



