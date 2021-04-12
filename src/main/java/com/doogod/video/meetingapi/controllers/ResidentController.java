package com.doogod.video.meetingapi.controllers;


import com.doogod.video.meetingapi.db.exceptions.IdentityNotFoundException;
import com.doogod.video.meetingapi.db.exceptions.ResidentNotFoundException;
import com.doogod.video.meetingapi.db.models.Identity;
import com.doogod.video.meetingapi.db.models.Resident;
import com.doogod.video.meetingapi.db.services.ResidentService;
import com.doogod.video.meetingapi.security.authentication.AuthenticationService;
import com.doogod.video.meetingapi.security.permissions.Permissions;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/residents")
@CrossOrigin(origins = "*")
public class ResidentController {

    @Autowired
    Jdbi jdbi;

    @Autowired
    AuthenticationService authService;

    @Autowired
    ResidentService residentService;

    Permissions permissions;

    Identity identity;

    @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Resident>> listResidents(@RequestHeader("Authorization") String auth) throws IdentityNotFoundException {
        permissions = authService.parsePermissions(auth);
        if (!permissions.contains(Permissions.CAN_LIST_RESIDENTS)) {
            return permissions.denied();
        }

        identity = authService.findByToken(auth);

        List<Resident> residents = residentService.findByResidencyId(identity.getResidencyId());

        return new ResponseEntity(residents, HttpStatus.OK);
    }

    @RequestMapping(path = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(
            value = "/postbody",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
            )
    public ResponseEntity<Resident> createResident(@RequestBody Resident resident, @RequestHeader("Authorization") String auth) throws IdentityNotFoundException {
        permissions = authService.parsePermissions(auth);
        if (!permissions.contains(Permissions.CAN_CREATE_RESIDENTS)) {
            return permissions.denied();
        }

        identity = authService.findByToken(auth);
        resident.setResidencyId(identity.getResidencyId());

        residentService.insert(resident);
        return new ResponseEntity<Resident>(resident, HttpStatus.OK);
    }

    @RequestMapping(path = "{residentId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteUser(@PathVariable("residentId") Integer residentId) {
        Resident resident;
        try {
            resident = residentService.findById(residentId);
        } catch (ResidentNotFoundException e) {
            JSONObject response = new JSONObject();
            response.put("message", "resident not found");
            return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
        }

        residentService.delete(resident);

        JSONObject response = new JSONObject();
        response.put("message", "resident " + resident.getName() + " deleted");

        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }

    @RequestMapping(path = "{residentId}/relatives", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listRelatives(@PathVariable("residentId") String residentId) {
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

    @RequestMapping(path = "{residentId}/relatives", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createRelative(@PathVariable("residentId") String residentId) {
        JSONObject relative1 = new JSONObject();
        relative1.put("id", 2);
        relative1.put("name", "some user");

        JSONObject response = new JSONObject();
        response.put("message", "relative created for user " + residentId);
        response.put("relative", relative1);

        return new ResponseEntity<String>(response.toString(), HttpStatus.CREATED);
    }

    @RequestMapping(path = "{residentId}/relatives/{relativeId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteUser(@PathVariable("residentId") String residentId, @PathVariable("relativeId") String relativeId) {
        JSONObject response = new JSONObject();
        response.put("message", "relative for user " + residentId + " with id " + relativeId + " deleted");
        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }
}
