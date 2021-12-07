package com.volodymyr.bookaro.order.domain;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum OrderStatus {

//    NEW {
//        @Override
//        public OrderStatus updateStatus(OrderStatus status) {
//            return switch (status) {
//                case PAID:
//                    return PAID;
//                case CANCELED:
//                   return CANCELED;
//                case ABANDONED:
//                    return ABANDONED;
//                default:
//                    return super.updateStatus(status);
//            }
//        }
//    },

    NEW {
        @Override
        public UpdateStatusResult updateStatus(OrderStatus status) {
            if (status == PAID) return UpdateStatusResult.ok(PAID);
            if (status == CANCELED) return UpdateStatusResult.revoked(CANCELED);
            if (status == ABANDONED) return UpdateStatusResult.revoked(ABANDONED);
            return super.updateStatus(status);
        }
    },

    PAID{
        @Override
        public UpdateStatusResult updateStatus(OrderStatus status) {
            if(status == SHIPPED) {
                return UpdateStatusResult.revoked(SHIPPED);
            }
            return super.updateStatus(status);
        }
    },
    CANCELED,
    ABANDONED,
    SHIPPED;

    public static Optional<OrderStatus> parseString(String value) {
        return Arrays.stream(values())
                .filter(it -> StringUtils.equalsIgnoreCase(it.name(), value))
                .findFirst();
    }

    public UpdateStatusResult updateStatus(OrderStatus status) {
        throw new IllegalArgumentException("Unable to mark " + this.name() + " order as " + status.name());
    }
}
