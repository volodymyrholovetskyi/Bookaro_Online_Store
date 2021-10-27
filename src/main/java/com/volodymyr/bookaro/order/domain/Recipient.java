package com.volodymyr.bookaro.order.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Recipient {

    String name;
    String phone;
    String street;
    String city;
    String zipCod;
    String email;
}
