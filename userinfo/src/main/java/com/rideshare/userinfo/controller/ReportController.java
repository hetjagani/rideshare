package com.rideshare.userinfo.controller;

import com.rideshare.userinfo.exception.UserDoesNotExistException;
import com.rideshare.userinfo.model.Report;
import com.rideshare.userinfo.exception.BadRequestException;
import com.rideshare.userinfo.security.UserPrincipal;
import com.rideshare.userinfo.service.IReportsService;
import com.rideshare.userinfo.service.ReportService;
import com.rideshare.userinfo.webentity.ReportInfo;
import com.rideshare.userinfo.webentity.PaginatedEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(path = "/users/{userID}/reports")
public class ReportController {

    @Autowired
    private IReportsService reportsService;

    @Autowired
    private RestTemplate restTemplate;

    private final Logger logger = LogManager.getLogger(ReportController.class);

    @GetMapping
    public ResponseEntity<PaginatedEntity<Report>> getAllReports(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);

            PaginatedEntity<Report> reportList = reportsService.getAllPaginated(token, page, limit);
            return ResponseEntity.ok(reportList);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping(path = "/")
    public ResponseEntity<Report> createReport(@AuthenticationPrincipal UserPrincipal userDetails, @RequestBody ReportInfo reportInfo) throws Exception {
        try {
            com.rideshare.userinfo.model.Report  report = new com.rideshare.userinfo.model.Report(Integer.parseInt(userDetails.getId()),
                    reportInfo.getReportedId(), reportInfo.getCategory(), reportInfo.getDescription());
            try {
                com.rideshare.userinfo.model.Report createdReport = reportsService.create(report);
                return ResponseEntity.ok(report);
            }catch (org.springframework.dao.DataIntegrityViolationException e){
                e.printStackTrace();
                throw new UserDoesNotExistException("Reported ID Does not exists!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
