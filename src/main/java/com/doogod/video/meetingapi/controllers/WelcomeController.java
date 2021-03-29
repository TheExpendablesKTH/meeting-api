package com.doogod.video.meetingapi.controllers;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@CrossOrigin(origins = "*")
public class WelcomeController {

    @RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> index() {
        JSONObject response = new JSONObject();
        response.put("message", "I am the api! I am also mocked");
        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }

}
