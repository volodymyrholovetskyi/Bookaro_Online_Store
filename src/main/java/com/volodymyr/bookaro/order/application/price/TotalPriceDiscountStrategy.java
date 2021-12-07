package com.volodymyr.bookaro.order.application.price;

import com.volodymyr.bookaro.order.domain.Order;
import com.volodymyr.bookaro.order.domain.OrderItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public class TotalPriceDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculate(Order order) {

        if (isGreaterOrEqual(order, 400)) {
            return lowestBookPrice(order.getItems());
        } else if (isGreaterOrEqual(order, 200)) {
            BigDecimal lowestPrice = lowestBookPrice(order.getItems());
            return lowestPrice.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }


    private boolean isGreaterOrEqual(Order order, int value) {
        return order.getItemsPrice().compareTo(BigDecimal.valueOf(value)) >= 0;
    }

    private BigDecimal lowestBookPrice(Set<OrderItem> items) {
        return items.stream()
                .map(x -> x.getBook().getPrice())
                .sorted()
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }
}
