package com.doogod.video.meetingapi.security.permissions;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;

public class Permissions {

    public static final Integer CAN_CREATE_ADMINS = 1;
    public static final Integer CAN_LIST_RESIDENTS = 2;
    public static final Integer CAN_CREATE_RESIDENTS = 3;

    private ArrayList<Integer> permissions;

    public Permissions() {
        this.permissions = new ArrayList<Integer>();
    }

    public static Permissions fromString(String toParse) {
        Permissions permissions = new Permissions();

        if (toParse == null) {
            return permissions;
        }

        toParse = toParse.replace("[","");
        toParse = toParse.replace("]","");
        ArrayList<String> parsed = new ArrayList<String>(Arrays.asList(toParse.split(",")));

        for (String permission : parsed) {
            permissions.add(Integer.parseInt(permission.strip()));
        }
        return permissions;
    }

    public ResponseEntity denied() {
        JSONObject response = new JSONObject();
        response.put("message", "forbidden");
        return new ResponseEntity(response.toString(), HttpStatus.FORBIDDEN);
    }

    public void add(Integer permission) {
        this.permissions.add(permission);
    }

    public boolean contains(Integer permission) {
        return this.permissions.contains(permission);
    }

    public String toString() {
        return this.permissions.toString();
    }

    public ArrayList<Integer> toArray() {
        return this.permissions;
    }
}
