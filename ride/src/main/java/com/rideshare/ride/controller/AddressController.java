package com.rideshare.ride.controller;

import com.rideshare.ride.model.Address;
import com.rideshare.ride.service.IAddressService;
import com.rideshare.ride.webentity.DeleteSuccess;
import com.rideshare.ride.webentity.PaginatedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/addresses")
public class AddressController {
    @Autowired
    private IAddressService addressService;

    @GetMapping
    public ResponseEntity<PaginatedEntity<Address>> searchAddresses(@RequestParam(required = false) String query, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) throws Exception {
        try {
            PaginatedEntity<Address> addresses = addressService.searchAddress(query, page, limit);
            return ResponseEntity.ok(addresses);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<Address> addAddress(@RequestBody Address address) throws Exception {
        try {
            Address createdAddress = addressService.create(address);
            return ResponseEntity.ok(createdAddress);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DeleteSuccess> deleteAddress(@PathVariable Integer id) throws Exception {
        try {
            if(addressService.delete(id)) {
                return ResponseEntity.ok(new DeleteSuccess(true));
            }
            return ResponseEntity.ok(new DeleteSuccess(false));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

}
