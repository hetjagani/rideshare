package com.rideshare.userinfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class License {
    private Integer id;
    private Integer userId;
    private String licenseNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String sex;
    private String height;
    private String weight;
    private String dob;
    private String expiry;
    private String issued;
    private String address1;
    private String address2;
    private String postcode;
    private String issuerRegion;
    private String issuerOrg;
    private Boolean verification;
}
