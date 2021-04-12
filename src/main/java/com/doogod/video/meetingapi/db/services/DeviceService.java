package com.doogod.video.meetingapi.db.services;

import com.doogod.video.meetingapi.db.exceptions.ResidencyNotFoundException;
import com.doogod.video.meetingapi.db.models.Device;
import com.doogod.video.meetingapi.db.models.Residency;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    @Autowired
    Jdbi jdbi;

    @Autowired
    ResidencyService residencyService;

    public void insert(Device device) throws ResidencyNotFoundException {
        Residency residency = residencyService.findByDevicePassphrase(device.getPassphrase());
        device.setResidencyId(residency.getId());
        var id = jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO devices(residency_id) values (:residency_id);")
                .bind("residency_id", device.getResidencyId())
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Long.class)
                .one()
        );
        device.setId(id.intValue());
    }

    public void delete(Device device) {
        jdbi.withHandle(handle -> handle.createUpdate("DELETE FROM devices WHERE id = :id")
                .bind("id", device.getId())
                .execute()
        );
    }
}
