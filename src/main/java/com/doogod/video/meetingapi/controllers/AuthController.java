package com.doogod.video.meetingapi.controllers;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
public class AuthController {

    @RequestMapping(path = "/device", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> device() {
        JSONObject device = new JSONObject();
        device.put("token", "some-device-token");

        JSONObject response = new JSONObject();
        response.put("message", "device authenticated");
        response.put("device", device);

        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }

    @RequestMapping(path = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> user() {
        JSONObject auth = new JSONObject();
        auth.put("token", "some-auth-token");

        JSONObject response = new JSONObject();
        response.put("message", "device authenticated");
        response.put("auth", auth);

        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }

    @RequestMapping(path = "/relative/init", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> relativeInit() {
        JSONObject response = new JSONObject();
        response.put("message", "code sent to registered phone");

        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }

    @RequestMapping(path = "/relative/validate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> relativeValidate() {
        JSONObject auth = new JSONObject();
        auth.put("token", "some-auth-token");

        JSONObject response = new JSONObject();
        response.put("message", "code authenticated");
        response.put("auth", auth);

        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }

    @RequestMapping(path = "/admin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> admin() {
        JSONObject auth = new JSONObject();
        auth.put("token", "some-auth-token");

        JSONObject response = new JSONObject();
        response.put("message", "credentials correct");
        response.put("auth", auth);

        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }
}
