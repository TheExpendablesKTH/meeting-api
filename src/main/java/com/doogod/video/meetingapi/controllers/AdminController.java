package com.doogod.video.meetingapi.controllers;


import com.doogod.video.meetingapi.db.exceptions.DatabaseException;
import com.doogod.video.meetingapi.db.exceptions.UsernameNotUniqueException;
import com.doogod.video.meetingapi.db.models.Admin;
import com.doogod.video.meetingapi.db.services.AdminService;
import com.doogod.video.meetingapi.db.services.IdentityService;
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
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    Jdbi jdbi;

    @Autowired
    IdentityService identityService;

    @Autowired
    AdminService adminService;

    @Autowired
    AuthenticationService authService;

    Permissions permissions;

    @RequestMapping(path = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(
            value = "/postbody",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin, @RequestHeader("Authorization") String auth) {
        permissions = authService.parsePermissions(auth);
        if (!permissions.contains(Permissions.CAN_CREATE_ADMINS)) {
            return permissions.denied();
        }

        adminService.insert(admin);

        try {
            identityService.insert(admin.createIdentity());
        } catch (DatabaseException e) {
            JSONObject response = new JSONObject();
            response.put("message", e.getClass().getSimpleName());

            HttpStatus code = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e instanceof UsernameNotUniqueException) {
                code = HttpStatus.BAD_REQUEST;
            }

            // if creating identity fails, ensure admin record
            // is not left around
            adminService.delete(admin);
            return new ResponseEntity(response.toString(), code);
        }

        return new ResponseEntity<Admin>(admin, HttpStatus.OK);
    }
}
