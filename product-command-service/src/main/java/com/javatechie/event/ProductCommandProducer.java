package com.javatechie.event;

import com.javatechie.dto.ProductEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCommandProducer {

    private final StreamBridge streamBridge;


    public boolean publishMessage(ProductEvent event) {
        return streamBridge.send("productEventProducer-out-0", event);
    }
}
