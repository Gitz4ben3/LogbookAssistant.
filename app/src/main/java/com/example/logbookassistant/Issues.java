package com.example.logbookassistant;

public class Issues {
    String userId, description, reportedTime, solvedTime, userInfo, myId, myPlace;

    public Issues() {
    }

    public Issues(String userId, String description, String reportedTime, String solvedTime, String userInfo, String myId, String myPlace) {
        this.userId = userId;
        this.description = description;
        this.reportedTime = reportedTime;
        this.solvedTime = solvedTime;
        this.userInfo = userInfo;
        this.myId = myId;
        this.myPlace = myPlace;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getMyPlace() {
        return myPlace;
    }

    public void setMyPlace(String myPlace) {
        this.myPlace = myPlace;
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

    public String getUserInfo() {
        return userInfo;
    }
}
