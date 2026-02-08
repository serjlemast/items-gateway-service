package org.serjlemast.controller;

import lombok.RequiredArgsConstructor;
import org.serjlemast.dto.ItemRequest;
import org.serjlemast.dto.ItemResponse;
import org.serjlemast.rpc.ItemsRpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ItemsController {
  private final ItemsRpcClient rpcClient;

  @GetMapping("/items")
  public ResponseEntity<ItemResponse> getItems() {
    ItemRequest request = new ItemRequest();
    return ResponseEntity.ok(rpcClient.fetchItems(request));
  }
}