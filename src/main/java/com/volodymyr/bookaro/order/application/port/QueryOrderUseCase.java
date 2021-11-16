package com.volodymyr.bookaro.order.application.port;

import com.volodymyr.bookaro.catalog.domain.Book;
import com.volodymyr.bookaro.order.domain.Order;
import com.volodymyr.bookaro.order.domain.OrderStatus;
import com.volodymyr.bookaro.order.domain.Recipient;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {

    List<RichOrder> findAll();

    Optional<RichOrder> findById(Long id);

    @Value
    class RichOrder {
        Long id;
        OrderStatus status;
        List<RichOrderItem> items;
        Recipient recipient;
        LocalDateTime createAt;

        public BigDecimal totalPrice(){
            return items.stream()
                    .map(item -> item.getBook().getPrice().multiply(new BigDecimal(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    @Value
    class RichOrderItem{
        Book book;
        int quantity;
    }

}
