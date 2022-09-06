package com.rideshare.userinfo.service;

import com.rideshare.userinfo.model.Report;
import com.rideshare.userinfo.webentity.PaginatedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReportService implements IReportsService{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.auth.url}")
    private String authURL;

    @Override
    public PaginatedEntity<Report> getAllPaginated(String token, Integer page, Integer limit) throws Exception {
        return null;
    }

    @Override
    public Report getById(Integer id) throws Exception {
        return null;
    }

    @Override
    public Report create(Report object) throws Exception {
        // create report info object in the database and return the original object with all info
        String createReportSQL = "INSERT INTO \"userinfo\".\"report\"(user_id, reported_id, category, description) VALUES(?,?,?,?)";
        Integer id = jdbcTemplate.update(createReportSQL, object.getUserId(), object.getReportedId(), object.getCategory(), object.getDescription());
        return getById(id);
    }

    @Override
    public Report update(Report object) throws Exception {
        return null;
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        return false;
    }
}
