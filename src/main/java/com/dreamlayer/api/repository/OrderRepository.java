package com.dreamlayer.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamlayer.api.modal.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
