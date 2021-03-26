package com.doogod.video.meetingapi.controllers;


import com.doogod.video.meetingapi.db.ResidentModel;
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

    @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResidentModel>> listUsers() {
        List<ResidentModel> residents = jdbi.withHandle(handle -> handle.createQuery("SELECT id, name FROM residents;")
                .registerRowMapper(ConstructorMapper.factory(ResidentModel.class))
                .mapTo(ResidentModel.class)
                .list()
        );

        return new ResponseEntity<List<ResidentModel>>(residents, HttpStatus.OK);
    }

    @RequestMapping(path = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(
            value = "/postbody",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
            )
    public ResponseEntity<ResidentModel> createUser(@RequestBody ResidentModel resident) {
        var newResident = jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO residents(name) values (:name);")
                .bind("name", resident.getName())
                .registerRowMapper(ConstructorMapper.factory(ResidentModel.class))
                .executeAndReturnGeneratedKeys()
                .mapTo(ResidentModel.class)
                .one()
        );
        return new ResponseEntity<ResidentModel>(newResident, HttpStatus.OK);
    }

    @RequestMapping(path = "{residentId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteUser(@PathVariable("residentId") String residentId) {
        var deletedResident = jdbi.withHandle(handle -> handle.createUpdate("DELETE FROM residents(name) WHERE id=:id")
                .bind("id", residentId)
                .registerRowMapper(ConstructorMapper.factory(ResidentModel.class))
                .executeAndReturnGeneratedKeys()
                .mapTo(ResidentModel.class)
                .one()
        );
        JSONObject response = new JSONObject();
        response.put("message", "resident \"" + deletedResident.getName() + "\" deleted");
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
