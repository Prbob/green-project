package com.brogrammers.brogrammers.domain.member;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String postal_code;
    private String middle_address;
    private String detailed_address;

    public Address(String postal_code, String middle_address, String detailed_address) {
        this.postal_code = postal_code;
        this.middle_address = middle_address;
        this.detailed_address = detailed_address;
    }

    protected Address(){}


}
