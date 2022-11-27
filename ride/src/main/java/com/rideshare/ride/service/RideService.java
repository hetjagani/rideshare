package com.rideshare.ride.service;

import com.rideshare.ride.mapper.RideIdMapper;
import com.rideshare.ride.mapper.RideTagMapper;
import com.rideshare.ride.mapper.RideWithAddressMapper;
import com.rideshare.ride.model.*;
import com.rideshare.ride.util.Pagination;
import com.rideshare.ride.webentity.PaginatedEntity;
import com.rideshare.ride.webentity.Ride;
import com.rideshare.ride.webentity.RideRating;
import facade.UserInfoFacade;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RideService implements IRideService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserInfoFacade userInfoService;

    private final String allRideTags = "SELECT *\n" +
            "FROM ride.tags, ride.ride_tags\n" +
            "WHERE tags.id = ride_tags.tag_id;";

    private final String rideTagsWithId = "SELECT *\n" +
            "FROM ride.tags, ride.ride_tags\n" +
            "WHERE tags.id = ride_tags.tag_id AND ride_tags.ride_id = ?;";

    private final String paginatedRideAddressQuery = "SELECT *\n" +
            "FROM ride.ride, ride.address as start_add, ride.address as end_add\n" +
            "WHERE (ride.start_address = start_add.id) AND (ride.end_address = end_add.id) AND status != 'DELETED'\n" +
            "LIMIT ? OFFSET ?;";

    private final String getRidesByUserIdQuery = "SELECT *\n" +
            "FROM ride.ride, ride.address as start_add, ride.address as end_add\n" +
            "WHERE (ride.start_address = start_add.id) AND (ride.end_address = end_add.id) AND user_id = ? AND status != 'DELETED'\n" +
            "LIMIT ? OFFSET ?;";

    private final String allRidesQuery = "SELECT *\n" +
            "FROM ride.ride, ride.address as start_add, ride.address as end_add\n" +
            "WHERE (ride.start_address = start_add.id) AND (ride.end_address = end_add.id);";

    private final String getByIdQuery = "SELECT *\n" +
            "FROM ride.ride, ride.address as start_add, ride.address as end_add\n" +
            "WHERE (ride.start_address = start_add.id) AND (ride.end_address = end_add.id) AND ride.id = ?;";

    private final String insertRideQuery = "INSERT INTO ride.ride(post_id, user_id, created_at, price_per_person, no_passengers, capacity, status, ride_time, start_address, end_address)\n" +
            "VALUES (?,?,?,?,?,?,?,?,?,?) RETURNING id;";

    private final String startRideQuery = "UPDATE ride.ride SET status='ACTIVE', started_at=? WHERE id = ? AND user_id = ?;";
    private final String stopRideQuery = "UPDATE ride.ride SET status='COMPLETED', ended_at=? WHERE id = ? AND user_id = ?;";

    private final String updateCapacityQuery = "UPDATE ride.ride SET capacity = ? WHERE id = ?";

    private final String deleteQuery = "UPDATE ride.ride SET status = ? WHERE ride.id = ?";

    private final String insertRideTagQuery = "INSERT INTO ride.ride_tags(ride_id, tag_id) VALUES(?,?);";

    private final String getNoOfRidesQuery = "SELECT COUNT(*) FROM ride.ride WHERE user_id = ?";

    private final String fetchRideRatingForUser = "SELECT ride_user_ratings.ride_id, ride_user_ratings.rating_id FROM ride.ride_user_ratings WHERE user_id = ?";

    private final String addRideRatingForUser = "INSERT INTO ride.ride_user_ratings(user_id, ride_id, rating_id) VALUES (?,?,?) RETURNING id";

    @Override
    public PaginatedEntity<Ride> getPaginated(String token, Integer page, Integer limit) throws Exception {
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

        Map<Integer, UserInfo> userInfoMap = userInfoService.getAllUsers(token);

        List<Ride> rideList = ridesWithAddress.stream().map((Ride r) -> {
            List<Tag> tagList = rideTagsMap.get(r.getId());
            r.setTags(tagList);
            r.setUser(userInfoMap.get(r.getUserId()));
            if(r.getEndedAt() != null && r.getStartedAt() != null)
                r.setDuration(Duration.between(r.getStartedAt().toInstant(), r.getEndedAt().toInstant()));
            return r;
        }).collect(Collectors.toList());

        return new PaginatedEntity<>(rideList, page, limit);
    }

    @Override
    public PaginatedEntity<Ride> searchRides(String token, Integer userId, Integer page, Integer limit) throws Exception {
        Integer offset = Pagination.getOffset(page, limit);
        List<Ride> ridesWithAddress = jdbcTemplate.query(getRidesByUserIdQuery, new RideWithAddressMapper(), userId, limit, offset);

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

        Map<Integer, UserInfo> userInfoMap = userInfoService.getAllUsers(token);

        List<Ride> rideList = ridesWithAddress.stream().map((Ride r) -> {
            List<Tag> tagList = rideTagsMap.get(r.getId());
            r.setTags(tagList);
            r.setUser(userInfoMap.get(r.getUserId()));
            if(r.getEndedAt() != null && r.getStartedAt() != null)
                r.setDuration(Duration.between(r.getStartedAt().toInstant(), r.getEndedAt().toInstant()));
            return r;
        }).collect(Collectors.toList());

        return new PaginatedEntity<>(rideList, page, limit);
    }

    @Override
    public List<Ride> getAll(String token) throws Exception {
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

        Map<Integer, UserInfo> userInfoMap = userInfoService.getAllUsers(token);

        List<Ride> rideList = ridesWithAddress.stream().map((Ride r) -> {
            List<Tag> tagList = rideTagsMap.get(r.getId());
            r.setTags(tagList);
            r.setUser(userInfoMap.get(r.getUserId()));
            if(r.getEndedAt() != null && r.getStartedAt() != null)
                r.setDuration(Duration.between(r.getStartedAt().toInstant(), r.getEndedAt().toInstant()));
            return r;
        }).collect(Collectors.toList());

        return rideList;
    }

    @Override
    public Ride getById(String token, Integer id) throws Exception {
        Ride rideWithAddress = jdbcTemplate.queryForObject(getByIdQuery, new RideWithAddressMapper(), id);

        List<RideTag> rideTags = jdbcTemplate.query(rideTagsWithId, new RideTagMapper(), id);

        Map<Integer, UserInfo> userInfoMap = userInfoService.getAllUsers(token);

        List<Tag> tagList = rideTags.stream().map((RideTag rt) -> rt.getTag()).collect(Collectors.toList());
        rideWithAddress.setTags(tagList);
        rideWithAddress.setUser(userInfoMap.get(rideWithAddress.getUserId()));
        if(rideWithAddress.getEndedAt() != null && rideWithAddress.getStartedAt() != null)
            rideWithAddress.setDuration(Duration.between(rideWithAddress.getStartedAt().toInstant(), rideWithAddress.getEndedAt().toInstant()));

        return rideWithAddress;
    }

    @Override
    public Ride getById(Integer id) throws Exception {
        // TODO: get information of user who has created this ride
        Ride rideWithAddress = jdbcTemplate.queryForObject(getByIdQuery, new RideWithAddressMapper(), id);

        List<RideTag> rideTags = jdbcTemplate.query(rideTagsWithId, new RideTagMapper(), id);

        List<Tag> tagList = rideTags.stream().map((RideTag rt) -> rt.getTag()).collect(Collectors.toList());
        rideWithAddress.setTags(tagList);
        if(rideWithAddress.getEndedAt() != null && rideWithAddress.getStartedAt() != null)
            rideWithAddress.setDuration(Duration.between(rideWithAddress.getStartedAt().toInstant(), rideWithAddress.getEndedAt().toInstant()));

        return rideWithAddress;
    }

    @Override
    public Ride create(String token, com.rideshare.ride.model.Ride ride) throws Exception {
        Integer id = jdbcTemplate.queryForObject(insertRideQuery, Integer.class, ride.getPostId(), ride.getUserId(), Timestamp.from(Instant.now()), ride.getPricePerPerson(), ride.getNoPassengers(), ride.getNoPassengers(), RideStatus.CREATED, new Timestamp(ride.getRideTime()), ride.getStartAddress(), ride.getEndAddress());

        if (ride.getTagIds() != null) {
            ride.getTagIds().stream().forEach((Integer tagId) -> {
                jdbcTemplate.update(insertRideTagQuery, id, tagId);
            });
        }
        Ride createdRide = getById(token, id);
        return createdRide;
    }

    @Override
    public Ride updateCapacity(Integer rideId, Integer capacity) throws Exception {
        jdbcTemplate.update(updateCapacityQuery, capacity, rideId);
        Ride updatedRide = getById(rideId);
        return updatedRide;
    }

    @Override
    public Ride startRide(String token, Integer rideId, Integer userId) throws Exception {
        Ride ride = getById(token, rideId);
        if(ride.getUserId() != userId) {
            throw new Exception("Cannot start other user's ride");
        }
        if(RideStatus.ACTIVE.equals(ride.getStatus())) {
            throw new Exception("Ride already started");
        }
        if(RideStatus.COMPLETED.equals(ride.getStatus())) {
            throw new Exception("Ride already completed");
        }

        if(ride.getRideTime().after(Timestamp.from(Instant.now()))) {
            throw new Exception("Cannot start ride before the ride time");
        }

        jdbcTemplate.update(startRideQuery, Timestamp.from(Instant.now()), rideId, userId);

        return getById(token, rideId);
    }

    @Override
    public Ride stopRide(String token, Integer rideId, Integer userId) throws Exception {
        Ride ride = getById(token, rideId);
        if(ride.getUserId() != userId) {
            throw new Exception("Cannot start other user's ride");
        }
        if(RideStatus.COMPLETED.equals(ride.getStatus())) {
            throw new Exception("Ride already completed");
        }

        if(ride.getRideTime().after(Timestamp.from(Instant.now()))) {
            throw new Exception("Cannot stop ride before the ride time");
        }

        jdbcTemplate.update(stopRideQuery, Timestamp.from(Instant.now()), rideId, userId);

        return getById(token, rideId);
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        Integer rowsAffected = jdbcTemplate.update(deleteQuery, RideStatus.DELETED, id);
        return rowsAffected != 0;
    }

    @Override
    public Integer getNoOfRides(Integer userId) throws Exception {
        Integer noOfRides = jdbcTemplate.queryForObject(getNoOfRidesQuery, Integer.class, userId);
        return noOfRides;
    }

    @Override
    public List<RideRating> checkRideForUserIfRated(Integer userId) throws Exception {
        List<RideRating> ridesRatedList = jdbcTemplate.query(fetchRideRatingForUser, new RideIdMapper(), userId);
        return ridesRatedList;
    }

    @Override
    public Integer createRideRatingForUser(Integer rideId, Integer userId, Integer rId) throws Exception {
        Integer id = jdbcTemplate.queryForObject(addRideRatingForUser, Integer.class, userId, rideId, rId);
        return id;
    }
}
