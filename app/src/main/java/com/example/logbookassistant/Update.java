package com.example.logbookassistant;

public class Update {
    String userId, description, reportedTime, solvedTime, userInfo, place, myId;

    public Update() {
    }

    public Update(String userId, String description, String reportedTime, String solvedTime, String userInfo, String place, String myId) {
        this.userId = userId;
        this.description = description;
        this.reportedTime = reportedTime;
        this.solvedTime = solvedTime;
        this.userInfo = userInfo;
        this.place = place;
        this.myId = myId;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public void setReportedTime(String reportedTime) {
        this.reportedTime = reportedTime;
    }

    public String getSolvedTime() {
        return solvedTime;
    }

    public void setSolvedTime(String solvedTime) {
        this.solvedTime = solvedTime;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }
}
