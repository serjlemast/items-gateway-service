package org.serjlemast.dto;

import org.serjlemast.model.Item;

import java.util.List;

public class ItemsResponse {
    private List<Item> items;

    public ItemsResponse() {}

    public ItemsResponse(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
