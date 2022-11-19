package com.rideshare.userinfo.webentity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class IdAnalyzerResponse {
    @Data
    @ToString
    public class Result {
        private String address1;
        private String address2;
        private Integer age;
        private Integer daysFromIssue;
        private Integer daysToExpiry;
        private String dob;
        private Integer dob_day;
        private Integer dob_month;
        private Integer dob_year;
        private String documentNumber;
        private String documentSide;
        private String documentType;
        private String expiry;
        private Integer expiry_day;
        private Integer expiry_month;
        private Integer expiry_year;
        private String firstName;
        private String middleName;
        private String lastName;
        private String fullName;
        private String height;
        private String internalId;
        private String issued;
        private Integer issued_day;
        private Integer issued_month;
        private Integer issued_year;
        private String issuerOrg_full;
        private String issuerOrg_iso2;
        private String issuerOrg_iso3;
        private String issuerOrg_region_abbr;
        private String issuerOrg_region_full;
        private String nationality_full;
        private String nationality_iso2;
        private String nationality_iso3;
        private String postcode;
        private String sex;
        private String weight;
        private String eyeColor;
        private String hairColor;
    }

    @Data
    @ToString
    public class Face {
        private Boolean isIdentical;
        private Float confidence;
    }

    @Data
    @ToString
    public class Verification {
        @Data
        @ToString
        public class VerificationResult {
            private Boolean face;
            private Boolean notexpired;
            private Boolean name;
            private Boolean dob;
            private Boolean documentNumber;
            private Boolean postcode;
            private Boolean address;
        }

        private VerificationResult result;
        private Boolean passed;
    }

    @Data
    @ToString
    public class Authentication {
        private Float score;
    }

    private Result result;
    private Face face;
    private Verification verification;
    private Authentication authentication;
    private String vaultid;
    private Integer matchrate;
    private Float executionTime;
    private String responseID;
}
