package com.doogod.video.meetingapi.controllers;

import com.doogod.video.meetingapi.chime.ChimeConnectionDetails;
import com.doogod.video.meetingapi.chime.AWSChimeWrapper;
import com.doogod.video.meetingapi.chime.SimpleJoin;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/calls")
@CrossOrigin(origins = "*")
public class CallController {

    @Autowired
    private SimpleJoin simpleJoin;

    @Value("${spring.tmpkey}")
    private String tmpKey;

    @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listCalls() {
        List<JSONObject> calls = new ArrayList<JSONObject>();
        JSONObject call1 = new JSONObject();
        call1.put("id", 1);
        call1.put("for_user", 2);
        call1.put("started_at", "some timestamp");
        calls.add(call1);

        JSONObject response = new JSONObject();
        response.put("message", "");
        response.put("calls", calls);

        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }

    @RequestMapping(path = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createCall() {
        JSONObject connectionDetails = new JSONObject();
        connectionDetails.put("...", "...");

        JSONObject call = new JSONObject();
        call.put("id", 1);
        call.put("for_user", 2);
        call.put("connection_details", connectionDetails);

        JSONObject response = new JSONObject();
        response.put("message", "call was created");
        response.put("call", call);

        return new ResponseEntity<String>(response.toString(), HttpStatus.CREATED);
    }

    @RequestMapping(path = "{callId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCall(@PathVariable("callId") String callId) {
        JSONObject connectionDetails = new JSONObject();
        connectionDetails.put("...", "...");

        JSONObject call = new JSONObject();
        call.put("id", 1);
        call.put("for_user", 2);
        call.put("connection_details", connectionDetails);

        JSONObject response = new JSONObject();
        response.put("message", "");
        response.put("call", call);
        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }

    @RequestMapping(path = "{callId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> terminateCall(@PathVariable("callId") String callId) {
        JSONObject response = new JSONObject();
        response.put("message", "call was terminated");
        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }

    @RequestMapping(path = "tmp/{externalId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChimeConnectionDetails> tmpConnectToCall(
            @PathVariable("externalId") String externalId,
            @RequestHeader("Authorization") String auth
    ) {
        if (!tmpKey.equals(auth)) {
            JSONObject response = new JSONObject();
            response.put("message", "unauthorized");
            return new ResponseEntity(response.toString(), HttpStatus.UNAUTHORIZED);
        }
        ChimeConnectionDetails details = simpleJoin.createMeetingIfItDoesNotExistAndAddAttendeeWithExternalId(externalId);
        return new ResponseEntity<ChimeConnectionDetails>(details, HttpStatus.OK);
    }
}
