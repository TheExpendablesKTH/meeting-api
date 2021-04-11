package com.doogod.video.meetingapi.controllers;

import com.doogod.video.meetingapi.db.exceptions.IdentityNotFoundException;
import com.doogod.video.meetingapi.db.models.Identity;
import com.doogod.video.meetingapi.security.authentication.AuthenticationService;
import com.doogod.video.meetingapi.security.permissions.Permissions;
import org.jdbi.v3.core.Jdbi;
import org.json.JSONObject;
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

    Permissions permissions;

    @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listUsers(@RequestHeader("Authorization") String auth) {
        try {
            identity = authService.findByToken(auth);
            permissions = authService.parsePermissions(auth);
        } catch (IdentityNotFoundException e) {
            return new ResponseEntity(new JSONObject().put("message", "unauthorized").toString(), HttpStatus.UNAUTHORIZED);
        }

        JSONObject response = new JSONObject();
        response.put("identity", new JSONObject(identity));
        response.put("permissions", permissions.toArray());
        response.put("token", auth.replace("Bearer ", ""));

        return new ResponseEntity(response.toString(), HttpStatus.OK);
    }
}
