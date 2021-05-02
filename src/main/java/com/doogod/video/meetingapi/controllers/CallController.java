package com.doogod.video.meetingapi.controllers;

import com.doogod.video.meetingapi.aws.chime.ChimeConnectionDetails;
import com.doogod.video.meetingapi.aws.chime.ChimeService;
import com.doogod.video.meetingapi.aws.chime.exceptions.MeetingExpiredException;
import com.doogod.video.meetingapi.aws.chime.exceptions.NoSuchCallException;
import com.doogod.video.meetingapi.controllers.request.body.CreateCallRequestBody;
import com.doogod.video.meetingapi.db.exceptions.*;
import com.doogod.video.meetingapi.db.models.*;
import com.doogod.video.meetingapi.db.services.CallService;
import com.doogod.video.meetingapi.db.services.RelativeService;
import com.doogod.video.meetingapi.db.services.ResidentService;
import com.doogod.video.meetingapi.security.authentication.AuthenticationService;
import com.doogod.video.meetingapi.security.permissions.Permissions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/call")
@CrossOrigin(origins = "*")
public class CallController {

    @Autowired
    AuthenticationService authService;

    @Autowired
    ChimeService chimeService;

    @Autowired
    ResidentService residentService;

    @Autowired
    RelativeService relativeService;

    @Autowired
    CallService callService;

    Identity identity;

    Permissions permissions;

    @RequestMapping(path = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(
            value = "/postbody",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ChimeConnectionDetails> createCall(@RequestHeader("Authorization") String auth, @RequestBody CreateCallRequestBody body) throws ResidentNotFoundException {
        try {
            identity = authService.findByToken(auth);
        } catch (IdentityNotFoundException e) {
            return new ResponseEntity(new JSONObject().put("message", "unauthorized").toString(), HttpStatus.UNAUTHORIZED);
        }

        permissions = authService.parsePermissions(auth);
        if (!permissions.contains(Permissions.CAN_START_CALLS)) {
            return permissions.denied();
        }

        Resident resident = residentService.findById(identity.getResidentId());
        List<Relative> relatives = relativeService.findByResident(resident)
                .stream()
                .filter(relative -> body.getRelatives().contains(relative.getId()))
                .collect(Collectors.toList());

        ChimeConnectionDetails connectionDetails = chimeService.createMeeting(resident);
        for (Relative relative : relatives) {
            try {
                chimeService.addRelativeToMeeting(connectionDetails.getMeetingId(), relative, resident);
            } catch (NoSuchCallException e) {
                // This shouldn't happen as we just created the call
            }
        }

        return new ResponseEntity(connectionDetails, HttpStatus.CREATED);
    }

    @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChimeConnectionDetails> getMyCallAsResident(@RequestHeader("Authorization") String auth) throws ResidentNotFoundException {
        try {
            identity = authService.findByToken(auth);
        } catch (IdentityNotFoundException e) {
            return new ResponseEntity(new JSONObject().put("message", "unauthorized").toString(), HttpStatus.UNAUTHORIZED);
        }

        permissions = authService.parsePermissions(auth);
        if (!permissions.contains(Permissions.CAN_LIST_CALLS)) {
            return permissions.denied();
        }

        Resident resident = residentService.findById(identity.getResidentId());

        try {
            ChimeConnectionDetails connectionDetails = chimeService.getMeetingForResident(resident);
            return new ResponseEntity(connectionDetails, HttpStatus.OK);
        } catch (NoSuchCallException e) {
            return new ResponseEntity(new JSONObject().put("message", "no such call").toString(), HttpStatus.NOT_FOUND);
        } catch (MeetingExpiredException e) {
            return new ResponseEntity(new JSONObject().put("message", "the requested call has expired").toString(), HttpStatus.GONE);
        }
    }

    @RequestMapping(path = "relative/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChimeConnectionDetails> getMyCallAsRelative(@PathVariable("code") String code) throws CallAttendeeNotFoundException {
        CallAttendee callAttendee;
        try {
            callAttendee = callService.findAttendeeByInvitationCode(code);
            Call call = callService.findCallById(callAttendee.getCallId());
            chimeService.throwIfCallExpired(call.getMeetingId());
        } catch (CallInviteNotFoundException e) {
            // TODO: improve the security here, probably should be some authentication since this is open to a brute force attack
            // TODO: improve the rate limit by recording and requiring authenticate with swedish mobile bank id

            // this delay is to mitigate the vulnerability to brute force guessing of codes
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException interruptedException) {}

            return new ResponseEntity(new JSONObject().put("message", "no such call").toString(), HttpStatus.NOT_FOUND);
        } catch (CallNotFoundException e) {
            return new ResponseEntity(new JSONObject().put("message", "no such call").toString(), HttpStatus.NOT_FOUND);
        } catch (MeetingExpiredException e) {
            return new ResponseEntity(new JSONObject().put("message", "the requested call has expired").toString(), HttpStatus.GONE);
        }

        Relative relative;
        try {
            relative = relativeService.findById(callAttendee.getRelativeId());
        } catch (RelativeNotFoundException e){
            return new ResponseEntity(new JSONObject().put("message", "relative not found").toString(), HttpStatus.NOT_FOUND);
        }
        try {
            ChimeConnectionDetails connectionDetails = chimeService.getMeetingForRelative(relative);
            return new ResponseEntity(connectionDetails, HttpStatus.OK);
        } catch (NoSuchCallException e) {
            return new ResponseEntity(new JSONObject().put("message", "no such call").toString(), HttpStatus.NOT_FOUND);
        } catch (MeetingExpiredException e) {
            return new ResponseEntity(new JSONObject().put("message", "the requested call has expired").toString(), HttpStatus.GONE);
        }
    }
}
