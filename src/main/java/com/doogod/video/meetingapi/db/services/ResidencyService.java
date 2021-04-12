package com.doogod.video.meetingapi.db.services;

import com.doogod.video.meetingapi.db.exceptions.IdentityNotFoundException;
import com.doogod.video.meetingapi.db.exceptions.ResidencyNotFoundException;
import com.doogod.video.meetingapi.db.models.Admin;
import com.doogod.video.meetingapi.db.models.Identity;
import com.doogod.video.meetingapi.db.models.Residency;
import com.doogod.video.meetingapi.db.models.Resident;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResidencyService {

    @Autowired
    Jdbi jdbi;

    public void insert(Residency residency) {
        // TODO
    }

    public Residency findByDevicePassphrase(String passphrase) throws ResidencyNotFoundException {
        try {
            return jdbi.withHandle(handle -> handle.createQuery("SELECT id, name, device_passphrase FROM residencies WHERE device_passphrase = :passphrase;")
                    .bind("passphrase", passphrase)
                    .registerRowMapper(ConstructorMapper.factory(Residency.class))
                    .mapTo(Residency.class)
                    .one()
            );
        } catch (IllegalStateException e) {
            throw new ResidencyNotFoundException();
        }
    }

    public void delete(Residency residency) {
        // TODO
    }
}
