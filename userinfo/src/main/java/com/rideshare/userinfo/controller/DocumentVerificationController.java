package com.rideshare.userinfo.controller;

import com.rideshare.userinfo.exception.BadRequestException;
import com.rideshare.userinfo.exception.ForbiddenException;
import com.rideshare.userinfo.facade.AuthServiceFacade;
import com.rideshare.userinfo.model.License;
import com.rideshare.userinfo.model.User;
import com.rideshare.userinfo.model.UserInfo;
import com.rideshare.userinfo.security.UserPrincipal;
import com.rideshare.userinfo.service.ILicenseService;
import com.rideshare.userinfo.service.IUserInfoService;
import com.rideshare.userinfo.service.IdAnalyzerService;
import com.rideshare.userinfo.webentity.DeleteSuccess;
import com.rideshare.userinfo.webentity.DocumentVerificationRequest;
import com.rideshare.userinfo.webentity.IdAnalyzerRequest;
import com.rideshare.userinfo.webentity.IdAnalyzerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/documents")
public class DocumentVerificationController {

    @Autowired
    private IdAnalyzerService idAnalyzerService;

    @Autowired
    private ILicenseService licenseService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private AuthServiceFacade authService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<License> getLicense(@PathVariable Integer id) throws Exception {
        try {
            return ResponseEntity.ok(licenseService.getById(id));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<License> getUserLicense(@AuthenticationPrincipal UserPrincipal userDetails) throws Exception {
        try {
            Integer userId = Integer.parseInt(userDetails.getId());
            return ResponseEntity.ok(licenseService.getByUserId(userId));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping
    private ResponseEntity<License> verifyDocument(@RequestBody DocumentVerificationRequest request,
                                                   @AuthenticationPrincipal UserPrincipal userDetails,
                                                   @RequestHeader HttpHeaders headers) throws Exception {
        try {
            Integer userId = Integer.parseInt(userDetails.getId());
            String token = headers.get("Authorization").get(0);
            // post license data to ID Analyzer service
            IdAnalyzerRequest idAnalyzerRequest = new IdAnalyzerRequest();
            idAnalyzerRequest.setFile_base64(request.getFileBase64());
            idAnalyzerRequest.setVerify_address(request.getAddress());
            idAnalyzerRequest.setVerify_dob(request.getDob());
            idAnalyzerRequest.setVerify_documentno(request.getDocumentNumber());
            idAnalyzerRequest.setVerify_name(request.getName());
            idAnalyzerRequest.setVerify_postcode(request.getPostcode());

            IdAnalyzerResponse idAnalyzerResponse = idAnalyzerService.verifyLicense(idAnalyzerRequest);

            // check response against the user data
            UserInfo loggedInUser = userInfoService.getById(token, userId);
            String loggedInUserName = loggedInUser.getFirstName().replaceAll("\\s", "").toLowerCase();
            String verificationFirstName = idAnalyzerResponse.getResult().getFirstName().replaceAll("\\s", "").toLowerCase();
            String verificationUserName = idAnalyzerResponse.getResult().getFullName().replaceAll("\\s", "").toLowerCase();

            if(!(loggedInUserName.contains(verificationFirstName) || verificationUserName.contains(loggedInUserName))) {
                throw new ForbiddenException("Please upload your own license");
            }

            if(!idAnalyzerResponse.getVerification().getPassed()) {
                throw new BadRequestException("License verification failed");
            }

            // save the response data to database if license is valid
            License license = new License();
            IdAnalyzerResponse.Result idAnalyzerResult = idAnalyzerResponse.getResult();
            license.setUserId(userId);
            license.setLicenseNumber(idAnalyzerResult.getDocumentNumber());
            license.setFirstName(idAnalyzerResult.getFirstName());
            license.setMiddleName(idAnalyzerResult.getMiddleName());
            license.setLastName(idAnalyzerResult.getLastName());
            license.setSex(idAnalyzerResult.getSex());
            license.setHeight(idAnalyzerResult.getHeight());
            license.setWeight(idAnalyzerResult.getWeight());
            license.setDob(idAnalyzerResult.getDob());
            license.setExpiry(idAnalyzerResult.getExpiry());
            license.setIssued(idAnalyzerResult.getIssued());
            license.setAddress1(idAnalyzerResult.getAddress1());
            license.setAddress2(idAnalyzerResult.getAddress2());
            license.setPostcode(idAnalyzerResult.getPostcode());
            license.setIssuerRegion(idAnalyzerResult.getIssuerOrg_region_full());
            license.setIssuerOrg(idAnalyzerResult.getIssuerOrg_full());
            license.setVerification(idAnalyzerResponse.getVerification().getPassed());

            License createdLicense = licenseService.create(license);

            // Add DRIVER to roles of user
            Set<String> userRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            userRoles.add("DRIVER");
            authService.updateRoles(new ArrayList<String>(userRoles), token);

            return ResponseEntity.ok(createdLicense);
        } catch (DuplicateKeyException dke) {
            dke.printStackTrace();
            throw new BadRequestException("user cannot upload multiple licenses");
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DeleteSuccess> deleteLicense(@PathVariable Integer id) throws Exception {
        boolean deleted = licenseService.delete(id);
        return ResponseEntity.ok(new DeleteSuccess(deleted));
    }
}
