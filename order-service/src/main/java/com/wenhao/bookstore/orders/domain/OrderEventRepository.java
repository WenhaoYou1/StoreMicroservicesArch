package com.wenhao.bookstore.orders.domain;

import org.springframework.data.jpa.repository.JpaRepository;

interface OrderEventRepository extends JpaRepository<OrderEventEntity, Long> {}
