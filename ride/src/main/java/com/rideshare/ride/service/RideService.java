package com.rideshare.ride.service;

import com.rideshare.ride.mapper.RideTagMapper;
import com.rideshare.ride.mapper.RideWithAddressMapper;
import com.rideshare.ride.model.RideStatus;
import com.rideshare.ride.model.RideTag;
import com.rideshare.ride.model.Tag;
import com.rideshare.ride.util.Pagination;
import com.rideshare.ride.webentity.PaginatedEntity;
import com.rideshare.ride.webentity.Ride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RideService implements IRideService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String allRideTags = "SELECT *\n" +
            "FROM ride.tags, ride.ride_tags\n" +
            "WHERE tags.id = ride_tags.tag_id;";

    private final String rideTagsWithId = "SELECT *\n" +
            "FROM ride.tags, ride.ride_tags\n" +
            "WHERE tags.id = ride_tags.tag_id AND ride_tags.ride_id = ?;";

    private final String paginatedRideAddressQuery = "SELECT *\n" +
            "FROM ride.ride, ride.address as start_add, ride.address as end_add\n" +
            "WHERE (ride.start_address = start_add.id) AND (ride.end_address = end_add.id)\n" +
            "LIMIT ? OFFSET ?;";

    private final String allRidesQuery = "SELECT *\n" +
            "FROM ride.ride, ride.address as start_add, ride.address as end_add\n" +
            "WHERE (ride.start_address = start_add.id) AND (ride.end_address = end_add.id);";

    private final String getByIdQuery = "SELECT *\n" +
            "FROM ride.ride, ride.address as start_add, ride.address as end_add\n" +
            "WHERE (ride.start_address = start_add.id) AND (ride.end_address = end_add.id) AND ride.id = ?;";

    private final String insertRideQuery = "INSERT INTO ride.ride(post_id, price_per_person, no_passengers, status, start_address, end_address)\n" +
            "VALUES (?,?,?,?,?,?) RETURNING id;";

    private final String deleteQuery = "DELETE FROM ride.ride WHERE ride.id = ?";

    private final String insertRideTagQuery = "INSERT INTO ride.ride_tags(ride_id, tag_id) VALUES(?,?);";

    @Override
    public PaginatedEntity<Ride> getPaginated(Integer page, Integer limit) throws Exception {
        Integer offset = Pagination.getOffset(page, limit);
        List<Ride> ridesWithAddress = jdbcTemplate.query(paginatedRideAddressQuery, new RideWithAddressMapper(), limit, offset);

        List<RideTag> rideTags = jdbcTemplate.query(allRideTags, new RideTagMapper());
        Map<Integer, List<Tag>> rideTagsMap = new HashMap<>();

        rideTags.forEach((RideTag rt) -> {
            List<Tag> tags;
            if(rideTagsMap.containsKey(rt.getRideId())) {
                tags = rideTagsMap.get(rt.getRideId());
            } else {
                tags = new ArrayList<>();
            }
            tags.add(rt.getTag());
            rideTagsMap.put(rt.getRideId(), tags);
        });

        List<Ride> rideList = ridesWithAddress.stream().map((Ride r) -> {
            List<Tag> tagList = rideTagsMap.get(r.getId());
            r.setTags(tagList);
            return r;
        }).collect(Collectors.toList());

        return new PaginatedEntity<>(rideList, page, limit);
    }

    @Override
    public List<Ride> getAll() throws Exception {
        List<Ride> ridesWithAddress = jdbcTemplate.query(allRidesQuery, new RideWithAddressMapper());

        List<RideTag> rideTags = jdbcTemplate.query(allRideTags, new RideTagMapper());
        Map<Integer, List<Tag>> rideTagsMap = new HashMap<>();

        rideTags.forEach((RideTag rt) -> {
            List<Tag> tags;
            if(rideTagsMap.containsKey(rt.getRideId())) {
                tags = rideTagsMap.get(rt.getRideId());
            } else {
                tags = new ArrayList<>();
            }
            tags.add(rt.getTag());
            rideTagsMap.put(rt.getRideId(), tags);
        });

        List<Ride> rideList = ridesWithAddress.stream().map((Ride r) -> {
            List<Tag> tagList = rideTagsMap.get(r.getId());
            r.setTags(tagList);
            return r;
        }).collect(Collectors.toList());

        return rideList;
    }

    @Override
    public Ride getById(Integer id) throws Exception {
        Ride rideWithAddress = jdbcTemplate.queryForObject(allRidesQuery, new RideWithAddressMapper(), id);

        List<RideTag> rideTags = jdbcTemplate.query(rideTagsWithId, new RideTagMapper(), id);

        List<Tag> tagList = rideTags.stream().map((RideTag rt) -> rt.getTag()).collect(Collectors.toList());
        rideWithAddress.setTags(tagList);

        return rideWithAddress;
    }

    @Override
    public Ride create(com.rideshare.ride.model.Ride ride) throws Exception {
        Integer id = jdbcTemplate.queryForObject(insertRideQuery, Integer.class, ride.getPostId(), ride.getPricePerPerson(), ride.getNoPassengers(), RideStatus.CREATED, ride.getStartAddress(), ride.getEndAddress());

        ride.getTagIds().stream().forEach((Integer tagId) -> {
            jdbcTemplate.update(insertRideTagQuery, id, tagId);
        });

        Ride createdRide = getById(id);
        return createdRide;
    }

    @Override
    public com.rideshare.ride.model.Ride update(com.rideshare.ride.model.Ride ride) throws Exception {
        return null;
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        jdbcTemplate.update(deleteQuery, id);
        return true;
    }
}
