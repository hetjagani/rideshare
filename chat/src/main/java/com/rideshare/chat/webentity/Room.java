package com.rideshare.chat.webentity;

import lombok.Data;
import java.util.Date;

import java.lang.String;

@Data
public class Room {
    private Integer initiatedFor;

    public Room() {}

    public Room(Integer initiatedFor) {
        this.initiatedFor = initiatedFor;
    }

    public Integer getInitiatedFor() {
        return initiatedFor;
    }

    public void setInitiatedFor(Integer initiatedFor) {
        this.initiatedFor = initiatedFor;
    }
}
