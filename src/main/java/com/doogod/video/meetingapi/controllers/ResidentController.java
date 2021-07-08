package com.doogod.video.meetingapi.controllers;


import com.doogod.video.meetingapi.db.exceptions.IdentityNotFoundException;
import com.doogod.video.meetingapi.db.exceptions.RelativeNotFoundException;
import com.doogod.video.meetingapi.db.exceptions.ResidentNotFoundException;
import com.doogod.video.meetingapi.db.models.Identity;
import com.doogod.video.meetingapi.db.models.Relative;
import com.doogod.video.meetingapi.db.models.Resident;
import com.doogod.video.meetingapi.db.services.RelativeService;
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

    @Autowired
    RelativeService relativeService;

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
    public ResponseEntity<List<Relative>> listRelatives(@PathVariable("residentId") Integer residentId, @RequestHeader("Authorization") String auth) {
        permissions = authService.parsePermissions(auth);
        if (!permissions.contains(Permissions.CAN_LIST_RELATIVES)) {
            return permissions.denied();
        }

        try {
            Resident resident = residentService.findById(residentId);
            List<Relative> relatives = relativeService.findByResident(resident);
            return new ResponseEntity(relatives, HttpStatus.OK);
        } catch (ResidentNotFoundException e) {
            return new ResponseEntity(new JSONObject().put("message", "resident not found").toString(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "{residentId}/relatives", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(
            value = "/postbody",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Relative> createRelative(@PathVariable("residentId") Integer residentId, @RequestBody Relative relative, @RequestHeader("Authorization") String auth) {
        permissions = authService.parsePermissions(auth);
        if (!permissions.contains(Permissions.CAN_CREATE_RELATIVES)) {
            return permissions.denied();
        }

        try {
            Resident resident = residentService.findById(residentId);
            relativeService.insert(relative, resident);
            return new ResponseEntity<Relative>(relative, HttpStatus.OK);
        } catch (ResidentNotFoundException e) {
            return new ResponseEntity(new JSONObject().put("message", "resident not found").toString(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "{residentId}/relatives/{relativeId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteRelative(@PathVariable("residentId") String residentId, @PathVariable("relativeId") String relativeId) {
        // TODO: Add a authentication check to deleteRelative for security.
        Relative relative;
        try {
            relative = relativeService.findById(Integer.parseInt(relativeId));
        } catch (RelativeNotFoundException e){
            return new ResponseEntity<String>(new JSONObject().put("message", "relative not found").toString(), HttpStatus.NOT_FOUND);
        }
        Resident resident;
        try {
            resident = residentService.findById(Integer.parseInt(residentId));
        } catch(ResidentNotFoundException e) {
            return new ResponseEntity<String>(new JSONObject().put("message", "resident not found").toString(), HttpStatus.NOT_FOUND);
        }
        List<Relative> relatives = relativeService.findByResident(resident);
        if(relatives.contains(relative)){
            relativeService.delete(relative);
            JSONObject response = new JSONObject();
            response.put("message", "relative for user " + residentId + " with id " + relativeId + " deleted");
            return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
        }
        else{
            JSONObject response = new JSONObject();
            response.put("message", "relative not in users contacts");
            return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
        }
    }
}
