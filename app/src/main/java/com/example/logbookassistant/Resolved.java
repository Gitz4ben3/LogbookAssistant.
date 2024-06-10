package com.example.logbookassistant;

public class Resolved {
    String userId, description, reportedTime, solvedTime, userInfo;

    public Resolved() {
    }

    public Resolved(String userId, String description, String reportedTime, String solvedTime, String userInfo) {
        this.userId = userId;
        this.description = description;
        this.reportedTime = reportedTime;
        this.solvedTime = solvedTime;
        this.userInfo = userInfo;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setReportedTime(String reportedTime) {
        this.reportedTime = reportedTime;
    }

    public void setSolvedTime(String solvedTime) {
        this.solvedTime = solvedTime;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getUserId() {
        return userId;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportedTime() {
        return reportedTime;
    }

    public String getSolvedTime() {
        return solvedTime;
    }

    public String getUserInfo() {
        return userInfo;
    }

}
