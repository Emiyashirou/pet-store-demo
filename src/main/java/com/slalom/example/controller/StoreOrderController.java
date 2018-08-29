package com.slalom.example.controller;

import com.slalom.example.domain.ApiResponse;
import com.slalom.example.domain.StoreOrder;
import com.slalom.example.repository.StoreOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/store/order")
public class StoreOrderController {
    @Autowired
    private StoreOrderRepository storeOrderRepository;

    @RequestMapping(value="/{orderId}", method=RequestMethod.GET)
    public HttpEntity<Object> findById(@PathVariable("orderId") final Long orderId){
        if(!(orderId >= 1 && orderId <= 10)){
            return new ResponseEntity<>(new ApiResponse(400, "BAD REQUEST", "Order id should be >= 1 and <= 10"), HttpStatus.BAD_REQUEST);
        }
        StoreOrder order = storeOrderRepository.findById(orderId);
        if(null == order){
            return new ResponseEntity<>(new ApiResponse(404, "NOT FOUND", "Cannot find order with ID " + orderId), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<StoreOrder> createOrder(@Valid @RequestBody final StoreOrder order){
        StoreOrder newOrder = storeOrderRepository.save(order);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @RequestMapping(value="/{orderId}", method=RequestMethod.DELETE)
    public HttpEntity<Object> deleteById(@PathVariable("orderId") final Long orderId, @RequestHeader("api_key") String apiKey){
        if(!apiKey.equals("special-key")){
            return new ResponseEntity<>(new ApiResponse(401, "Unauthorized", "Need Authorization"), HttpStatus.UNAUTHORIZED);
        }
        if(orderId <= 0){
            return new ResponseEntity<>(new ApiResponse(400, "BAD REQUEST", "Order ID must be positive Integer"), HttpStatus.BAD_REQUEST);
        }
        if(storeOrderRepository.findById(orderId) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        storeOrderRepository.deleteById(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}