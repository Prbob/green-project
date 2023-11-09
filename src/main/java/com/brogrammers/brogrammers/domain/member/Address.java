package com.brogrammers.brogrammers.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
public class Address {
    private String postal_code;
    private String middle_address;
    private String detailed_address;

    @Builder
    public Address(String postal_code, String middle_address, String detailed_address) {
        this.postal_code = postal_code;
        this.middle_address = middle_address;
        this.detailed_address = detailed_address;
    }


}
