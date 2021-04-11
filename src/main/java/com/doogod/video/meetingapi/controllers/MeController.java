package com.doogod.video.meetingapi.controllers;

import com.doogod.video.meetingapi.db.exceptions.IdentityNotFoundException;
import com.doogod.video.meetingapi.db.models.Identity;
import com.doogod.video.meetingapi.security.authentication.AuthenticationService;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
@CrossOrigin(origins = "*")
public class MeController {

    @Autowired
    Jdbi jdbi;

    @Autowired
    AuthenticationService authService;

    Identity identity;

    @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Identity> listUsers(@RequestHeader("Authorization") String auth) {
        try {
            identity = authService.findByToken(auth);
        } catch (IdentityNotFoundException e) {
            org.json.JSONObject response = new org.json.JSONObject();
            response.put("message", "unauthorized");
            return new ResponseEntity(response.toString(), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity(identity, HttpStatus.OK);
    }
}
