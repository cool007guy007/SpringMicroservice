package com.service.orderservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.service.orderservice.entities.Order; 
 
 
 public interface OrderRepository extends JpaRepository<Order, Long> { 
 } 

