package com.javatechie.event;

import com.javatechie.dto.ProductEvent;
import com.javatechie.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class ProductQueryConsumer {


    private final ProductQueryService productQueryService;

    @Bean
    public Consumer<ProductEvent> consumeProductEvents() {
        return productQueryService::processProductEvents;
    }
}
