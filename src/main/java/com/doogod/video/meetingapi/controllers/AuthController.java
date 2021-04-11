package com.doogod.video.meetingapi.controllers;

import com.doogod.video.meetingapi.db.exceptions.IdentityNotFoundException;
import com.doogod.video.meetingapi.db.models.Admin;
import com.doogod.video.meetingapi.db.services.AdminService;
import com.doogod.video.meetingapi.security.authentication.AuthenticationService;
import org.jdbi.v3.core.Jdbi;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authenticate")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    Jdbi jdbi;

    @Autowired
    AdminService adminService;

    @Autowired
    AuthenticationService authService;

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
    @PostMapping(
            value = "/postbody",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<String> admin(@RequestBody Admin admin) {
        try {
            JSONObject response = new JSONObject();
            response.put("token", authService.login(admin.getIdentity()));
            return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
        } catch (IdentityNotFoundException e) {
            JSONObject response = new JSONObject();
            response.put("message", e.getClass().getSimpleName());
            return new ResponseEntity(response.toString(), HttpStatus.FORBIDDEN);
        }
    }
}
