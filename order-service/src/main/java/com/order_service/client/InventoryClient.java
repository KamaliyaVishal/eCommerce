package com.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "INVENTORY-SERVICE", path = "product/api/v1")
public interface InventoryClient {

    @PutMapping("/{id}/reduce-stock")
    Double reduceStock(@PathVariable("id") Long id, @RequestParam("quantity") Integer quantity);
}
