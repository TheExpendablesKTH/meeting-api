package com.doogod.video.meetingapi.controllers;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listUsers() {
        List<JSONObject> users = new ArrayList<JSONObject>();
        JSONObject user1 = new JSONObject();
        user1.put("id", 1);
        user1.put("name", "some user");
        users.add(user1);

        JSONObject response = new JSONObject();
        response.put("message", "");
        response.put("users", users);

        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }

    @RequestMapping(path = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser() {
        JSONObject user1 = new JSONObject();
        user1.put("id", 2);
        user1.put("name", "some user");

        JSONObject response = new JSONObject();
        response.put("message", "user created");
        response.put("user", user1);

        return new ResponseEntity<String>(response.toString(), HttpStatus.CREATED);
    }

    @RequestMapping(path = "{userId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId) {
        JSONObject response = new JSONObject();
        response.put("message", "user " + userId + " deleted");
        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }

    @RequestMapping(path = "{userId}/relatives", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listRelatives(@PathVariable("userId") String userId) {
        List<JSONObject> users = new ArrayList<JSONObject>();
        JSONObject relative1 = new JSONObject();
        relative1.put("id", 1);
        relative1.put("phone", "+46...");
        relative1.put("name", "some relative");
        users.add(relative1);

        JSONObject response = new JSONObject();
        response.put("message", "");
        response.put("relatives", users);

        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }

    @RequestMapping(path = "{userId}/relatives", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createRelative(@PathVariable("userId") String userId) {
        JSONObject relative1 = new JSONObject();
        relative1.put("id", 2);
        relative1.put("name", "some user");

        JSONObject response = new JSONObject();
        response.put("message", "relative created for user " + userId);
        response.put("relative", relative1);

        return new ResponseEntity<String>(response.toString(), HttpStatus.CREATED);
    }

    @RequestMapping(path = "{userId}/relatives/{relativeId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId, @PathVariable("relativeId") String relativeId) {
        JSONObject response = new JSONObject();
        response.put("message", "relative for user " + userId + " with id " + relativeId + " deleted");
        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }
}
