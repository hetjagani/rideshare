package com.rideshare.userinfo.webentity;

import lombok.Data;

@Data
public class IdAnalyzerRequest {
    private String apikey;
    private Integer accuracy;
    private Boolean authenticate;
    private Boolean verify_expiry;
    private String verify_documentno;
    private String verify_name;
    private String verify_dob;
    private String verify_address;
    private String verify_postcode;
    private String type = "D";
    private String file_base64;
}
