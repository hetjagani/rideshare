package com.rideshare.userinfo.service;

import com.rideshare.userinfo.mapper.DetailedReportMapper;
import com.rideshare.userinfo.mapper.ReportInfoMapper;
import com.rideshare.userinfo.mapper.UserInfoMapper;
import com.rideshare.userinfo.model.Report;
import com.rideshare.userinfo.model.User;
import com.rideshare.userinfo.model.UserInfo;
import com.rideshare.userinfo.util.Pagination;
import com.rideshare.userinfo.webentity.DetailedReport;
import com.rideshare.userinfo.webentity.PaginatedEntity;
import com.rideshare.userinfo.webentity.ReportInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService implements DAOInterface<DetailedReport>{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.auth.url}")
    private String authURL;

    @Override
    public List<DetailedReport> getAll(){
        return null;
    }
    @Override
    public List<DetailedReport> getAllPaginated(Integer page, Integer limit) throws Exception {
        int offset = Pagination.getOffset(page, limit);
        String reportsInfoSQL = String.format("SELECT *" +
                " FROM \"userinfo\".\"report\" as report" +
                " , \"userinfo\".\"userinfo\" as reporter" +
                " , \"userinfo\".\"userinfo\" as reported" +
                " WHERE reporter.id != reported.id " +
                " AND (report.user_id = reporter.id " +
                " AND report.reported_id = reported.id)" +
                " ORDER BY report.id ASC " +
                " LIMIT ? OFFSET ?" );
        List<DetailedReport> reportsInfoList = jdbcTemplate.query(reportsInfoSQL, new DetailedReportMapper(),
                                                new Object[]{limit, offset});
        return reportsInfoList;
    }

    @Override
    public DetailedReport getById(Integer id) throws Exception {
        String sql =String.format("SELECT *" +
                " FROM \"userinfo\".\"report\" as report" +
                " , \"userinfo\".\"userinfo\" as reporter" +
                " , \"userinfo\".\"userinfo\" as reported" +
                " WHERE (report.id = ?) " +
                " AND (reporter.id != reported.id)" +
                " AND (report.user_id = reporter.id " +
                " AND report.reported_id = reported.id)");

        return jdbcTemplate.queryForObject(sql, new DetailedReportMapper(), id);
    }

    @Override
    public DetailedReport create(DetailedReport object) throws Exception {

        // create report info object in the database and return the original object with all info
        String createReportSQL = "INSERT INTO \"userinfo\".\"report\"(user_id, reported_id, title, category, description) VALUES(?, ?,?,?,?) RETURNING id";
        Integer id = jdbcTemplate.queryForObject(createReportSQL, Integer.class, object.getUserInfo().getId(), object.getReportedUserInfo().getId(), object.getTitle(), object.getCategory(), object.getDescription());
        return getById(id);
    }

    @Override
    public DetailedReport update(DetailedReport object) throws Exception {
        // create report info object in the database and return the original object with all info
        String createReportSQL = "UPDATE \"userinfo\".\"report\" " +
                "SET user_id = ?, " +
                "reported_id = ?, " +
                "title = ?, " +
                "category = ?, " +
                "description = ? " +
                "WHERE id = ? " +
                "RETURNING id";
        Integer id = jdbcTemplate.queryForObject(createReportSQL, Integer.class, object.getUserInfo().getId(), object.getReportedUserInfo().getId(), object.getTitle(), object.getCategory(), object.getDescription(), object.getReportId());
        return getById(id);
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        String deleteReportSQL = "DELETE FROM \"userinfo\".\"report\" WHERE id = ?";
        int deleted = jdbcTemplate.update(deleteReportSQL, id);
        return deleted == 1 ? true: false;
    }
}
