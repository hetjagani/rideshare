package com.rideshare.userinfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
public class Report {

    private int id;
    private int userId;
    private int reportedId;
    private String title;
    private String category;
    private String description;

    public Report(){}

    public Report(int id, int userId, int reportedId, String title, String category, String description) {
        this.id = id;
        this.userId = userId;
        this.reportedId = reportedId;
        this.title = title;
        this.category = category;
        this.description = description;
    }

    public Report(int userId, int reportedId, String title, String category, String description) {
        this.userId = userId;
        this.reportedId = reportedId;
        this.title = title;
        this.category = category;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", userId=" + userId +
                ", reportedId=" + reportedId +
                ", title=" + title +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
