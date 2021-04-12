package com.doogod.video.meetingapi.controllers;

import com.doogod.video.meetingapi.db.exceptions.DatabaseException;
import com.doogod.video.meetingapi.db.exceptions.IdentityNotFoundException;
import com.doogod.video.meetingapi.db.models.Admin;
import com.doogod.video.meetingapi.db.models.Device;
import com.doogod.video.meetingapi.db.models.Resident;
import com.doogod.video.meetingapi.db.services.AdminService;
import com.doogod.video.meetingapi.db.services.DeviceService;
import com.doogod.video.meetingapi.db.services.IdentityService;
import com.doogod.video.meetingapi.db.services.ResidentService;
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
@RequestMapping("/authenticate")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    Jdbi jdbi;

    @Autowired
    AdminService adminService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    ResidentService residentService;

    @Autowired
    IdentityService identityService;

    @Autowired
    AuthenticationService authService;

    Permissions permissions;

    @RequestMapping(path = "device", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(
            value = "/postbody",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<String> device(@RequestBody Device device) {
        try {
            deviceService.insert(device);
            identityService.insert(device.createIdentity());

            String token = authService.login(device);

            JSONObject response = new JSONObject();
            response.put("token", token);
            return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
        } catch (DatabaseException e) {
            JSONObject response = new JSONObject();
            response.put("message", "unauthorized");

            return new ResponseEntity(response.toString(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(path = "/resident", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(
            value = "/postbody",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<String> resident(@RequestBody Resident resident, @RequestHeader("Authorization") String auth) {
        permissions = authService.parsePermissions(auth);
        if (!permissions.contains(Permissions.CAN_LOGIN_RESIDENTS)) {
            return permissions.denied();
        }

        try {
            JSONObject response = new JSONObject();
            Resident fromDB = residentService.findById(resident.getId());
            identityService.insert(fromDB.createIdentity());
            response.put("token", authService.login(resident));
            return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
        } catch (IdentityNotFoundException e) {
            JSONObject response = new JSONObject();
            response.put("message", e.getClass().getSimpleName());
            return new ResponseEntity(response.toString(), HttpStatus.UNAUTHORIZED);
        } catch (DatabaseException e) {
            JSONObject response = new JSONObject();
            response.put("message", "unauthorized");

            return new ResponseEntity(response.toString(), HttpStatus.UNAUTHORIZED);
        }
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
    public ResponseEntity<String> admin(@RequestBody Admin admin, @RequestHeader("Authorization") String auth) {
        permissions = authService.parsePermissions(auth);
        if (!permissions.contains(Permissions.CAN_LOGIN_ADMINS)) {
            return permissions.denied();
        }

        try {
            JSONObject response = new JSONObject();
            response.put("token", authService.login(admin));
            return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
        } catch (IdentityNotFoundException e) {
            JSONObject response = new JSONObject();
            response.put("message", e.getClass().getSimpleName());
            return new ResponseEntity(response.toString(), HttpStatus.UNAUTHORIZED);
        }
    }
}
