package com.rideshare.userinfo.webentity;

import lombok.Data;

@Data
public class DocumentVerificationRequest {
    private String fileBase64;
    private String address;
    private String dob;
    private String documentNumber;
    private String name;
    private String postcode;
}
