package com.javatechie.service;

import com.javatechie.dto.ProductEvent;
import com.javatechie.entity.Product;
import com.javatechie.event.ProductCommandProducer;
import com.javatechie.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductCommandService {


    private final ProductRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ProductCommandProducer productCommandProducer;

    @Transactional
    public Product createProduct(ProductEvent productEvent) {
        Product productDO = repository.save(productEvent.getProduct());
        ProductEvent event = new ProductEvent("CreateProduct", productDO);
        //kafkaTemplate.send("product-event-topic", event);
        boolean publishMessage = productCommandProducer.publishMessage(event);
        if (!publishMessage)
            throw new RuntimeException("Error Publishing CreateProduct command.");
        return productDO;
    }

    @Transactional
    public Product updateProduct(long id, ProductEvent productEvent) {
        Product existingProduct = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("No product found with id: " + id));
        Product newProduct = productEvent.getProduct();
        existingProduct.setName(newProduct.getName());
        existingProduct.setPrice(newProduct.getPrice());
        existingProduct.setDescription(newProduct.getDescription());
        Product productDO = repository.save(existingProduct);
        ProductEvent event = new ProductEvent("UpdateProduct", productDO);
        //kafkaTemplate.send("product-event-topic", event);
        boolean publishMessage = productCommandProducer.publishMessage(event);
        if (!publishMessage)
            throw new RuntimeException("Error Publishing CreateProduct command.");
        return productDO;
    }

}
