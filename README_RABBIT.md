RabbitMQ RPC example for the Store service

This file contains example classes for the Store (first) service that the Gateway (this project) can call via RabbitMQ RPC.

1) DTOs (shared with the gateway)

```java
// ItemsRequest.java
package org.example.store.dto;

public class ItemsRequest {
    private String query;
    public ItemsRequest() {}
    public ItemsRequest(String query) { this.query = query; }
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
}

// ItemsResponse.java
package org.example.store.dto;

import java.util.List;
import org.example.store.model.Item;

public class ItemsResponse {
    private List<Item> items;
    public ItemsResponse() {}
    public ItemsResponse(List<Item> items) { this.items = items; }
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}
```

2) `Item` record example (use in the store service)

```java
package org.example.store.model;

import java.time.Instant;

public record Item(String guid, String title, String content, String link, Instant date) {}
```

3) Listener (RPC handler) in the Store service

```java
package org.example.store.rpc;

import org.example.store.dto.ItemsRequest;
import org.example.store.dto.ItemsResponse;
import org.example.store.model.Item;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemsRpcListener {

    // Spring AMQP will send the return value back as the RPC reply
    @RabbitListener(queues = "items.rpc.queue")
    public ItemsResponse handleItemsRequest(ItemsRequest request) {
        // Example: load items from DB, here we return a mocked list
        List<Item> items = loadItemsFromDbOrService(request.getQuery());
        return new ItemsResponse(items);
    }

    private List<Item> loadItemsFromDbOrService(String query) {
        // implement real DB access here. Example stub:
        return List.of(new Item("guid-1", "Title 1", "Content", "http://link", Instant.now()));
    }
}
```

Notes:
- Gateway (this project) calls `rabbitTemplate.convertSendAndReceive("items.exchange", "items.rpc", request)`.
- The listener returns an `ItemsResponse` object; with Jackson message converter the object will be serialized and returned.
- Use shared DTOs (or identical classes) on both sides to avoid serialization mismatch.
