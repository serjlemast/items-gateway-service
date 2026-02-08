package org.serjlemast.dto;

import lombok.Data;
import org.serjlemast.rpc.Item;

import java.util.List;

@Data
public class ItemResponse {
    private List<Item> items;
}
