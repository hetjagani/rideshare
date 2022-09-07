package com.rideshare.userinfo.controller;

import com.rideshare.userinfo.exception.UserDoesNotExistException;
import com.rideshare.userinfo.service.DAOInterface;
import com.rideshare.userinfo.webentity.DetailedReport;
import com.rideshare.userinfo.webentity.ReportInfo;
import com.rideshare.userinfo.webentity.PaginatedEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;


@RestController
@RequestMapping(path = "/users/{userID}/reports")
public class ReportController {

    @Autowired
    private DAOInterface reportsService;

    @Autowired
    private RestTemplate restTemplate;

    private final Logger logger = LogManager.getLogger(ReportController.class);

    @GetMapping
    public ResponseEntity<PaginatedEntity<ReportInfo>> getAllReports(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) throws Exception {
        try {
            List<ReportInfo> reportList = reportsService.getAllPaginated(page, limit);
            PaginatedEntity<ReportInfo> paginatedList = new PaginatedEntity<ReportInfo>(reportList, page, limit);
            return ResponseEntity.ok(paginatedList);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<DetailedReport> createReport(@RequestBody ReportInfo reportInfo, @PathVariable Integer userID) throws Exception {
        try {

            com.rideshare.userinfo.webentity.DetailedReport detailedReport =
                    new com.rideshare.userinfo.webentity.DetailedReport(
                                new com.rideshare.userinfo.webentity.UserInfo(userID, "", "", ""),
                                new com.rideshare.userinfo.webentity.UserInfo(reportInfo.getReportedId(), "", "", ""),
                                reportInfo.getTitle(),
                                reportInfo.getCategory(),
                                reportInfo.getDescription()
                            );
            try {
                com.rideshare.userinfo.webentity.DetailedReport createdReport = (DetailedReport) reportsService.create(detailedReport);
                return ResponseEntity.ok(createdReport);
            }catch (org.springframework.dao.DataIntegrityViolationException e){
                e.printStackTrace();
                throw new UserDoesNotExistException("Reported ID Does not exists!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/{reportID}")
    public ResponseEntity<DetailedReport> getReportInfoById(@PathVariable Integer reportID) throws Exception {
        try {
            DetailedReport reportInfo = (DetailedReport) reportsService.getById(reportID);
            reportInfo.setReportId(reportID);
            return ResponseEntity.ok(reportInfo);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping(path = "/{reportID}")
    public ResponseEntity<DetailedReport> updateReportInfoById(@PathVariable Integer reportID, @PathVariable Integer userID, @RequestBody ReportInfo reportInfo) throws Exception {
        try {
            com.rideshare.userinfo.webentity.DetailedReport detailedReport =
                    new com.rideshare.userinfo.webentity.DetailedReport(
                            new com.rideshare.userinfo.webentity.UserInfo(userID, "", "", ""),
                            new com.rideshare.userinfo.webentity.UserInfo(reportInfo.getReportedId(), "", "", ""),
                            reportInfo.getTitle(),
                            reportInfo.getCategory(),
                            reportInfo.getDescription()
                    );
            detailedReport.setReportId(reportID);
            try {
                com.rideshare.userinfo.webentity.DetailedReport createdReport = (DetailedReport) reportsService.update(detailedReport);
                return ResponseEntity.ok(createdReport);
            }catch (org.springframework.dao.DataIntegrityViolationException e){
                e.printStackTrace();
                throw new UserDoesNotExistException("Reported ID Does not exists!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping(path = "/{reportID}")
    public ResponseEntity<DetailedReport> deleteReportById(@PathVariable Integer reportID) throws Exception {
        try {
            reportsService.delete(reportID);
            return ResponseEntity.ok(null);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
