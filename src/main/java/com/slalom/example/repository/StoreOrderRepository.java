package com.slalom.example.repository;

import org.springframework.data.repository.Repository;

import com.slalom.example.domain.StoreOrder;
import org.springframework.transaction.annotation.Transactional;

public interface StoreOrderRepository extends Repository<StoreOrder, Long> {
    StoreOrder findById(Long id);

    StoreOrder save(StoreOrder order);

    @Transactional
    Long deleteById(Long id);
}
