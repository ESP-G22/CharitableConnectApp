package dev.n0ne1eft.charitableconnect;

import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

import api.UserProfile;
import layout.OutputPair;

class GetUserTask extends AsyncTask<String, String, UserProfile> {
    private String token;
    private int userID;

    public GetUserTask(String token, int userID) {
        super();
        this.token = token;
        this.userID = userID;
    }
    protected UserProfile doInBackground(String... params) {
        UserProfile user = null;
        try {
            user = new UserProfile(token, userID);
        } catch (Exception err) {
            err.printStackTrace();
        }
        return user;
    }
}
