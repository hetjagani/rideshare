package com.rideshare.userinfo.service;

import com.rideshare.userinfo.mapper.LicenseMapper;
import com.rideshare.userinfo.model.License;
import com.rideshare.userinfo.util.Pagination;
import com.rideshare.userinfo.webentity.PaginatedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LicenseService implements ILicenseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String getAllPaginatedQuery = "SELECT * FROM userinfo.license LIMIT ? OFFSET ?;";
    private static final String getByIdQuery = "SELECT * FROM userinfo.license WHERE id = ?;";
    private static final String getByUserIdQuery = "SELECT * FROM userinfo.license WHERE user_id = ?;";
    private static final String createQuery = "INSERT INTO userinfo.license(\n" +
            "\tuser_id, license_number, first_name, middle_name, last_name, sex, height, weight, dob, expiry, issued, address1, address2, postcode, issuer_region, issuer_org, verification)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";
    private static final String deleteQuery = "DELETE FROM userinfo.license WHERE id = ?";

    @Override
    public PaginatedEntity<License> getAllPaginated(Integer page, Integer limit) throws Exception {
        Integer offset = Pagination.getOffset(page, limit);
        List<License> licenseList = jdbcTemplate.query(getAllPaginatedQuery, new LicenseMapper(), limit, offset);
        return new PaginatedEntity<License>(licenseList, page, limit);
    }

    @Override
    public License getById(Integer id) throws Exception {
        return jdbcTemplate.queryForObject(getByIdQuery, new LicenseMapper(), id);
    }

    @Override
    public License getByUserId(Integer userId) throws Exception {
        return jdbcTemplate.queryForObject(getByUserIdQuery, new LicenseMapper(), userId);
    }

    @Override
    public License create(License license) throws Exception {
        Integer createdId = jdbcTemplate.queryForObject(createQuery, Integer.class, license.getUserId(), license.getLicenseNumber(), license.getFirstName(), license.getMiddleName(), license.getLastName(), license.getSex(), license.getHeight(), license.getWeight(), license.getDob(), license.getExpiry(), license.getIssued(), license.getAddress1(), license.getAddress2(), license.getPostcode(), license.getIssuerRegion(), license.getIssuerOrg(), license.getVerification());
        return getById(createdId);
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        int affectedRows = jdbcTemplate.update(deleteQuery, id);
        return affectedRows != 0;
    }
}
