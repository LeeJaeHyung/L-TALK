package com.ltalk.service;

public class FriendService {


    public void addFriend() {
        DataService dataService = new DataService();
        dataService.requestFriend();
    }
}
