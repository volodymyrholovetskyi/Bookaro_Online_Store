package com.volodymyr.bookaro.order.application.port;

import com.volodymyr.bookaro.order.application.RichOrder;

import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {

    List<RichOrder> findAll();

    Optional<RichOrder> findById(Long id);

}
