package com.doogod.video.meetingapi.db.services;

import com.doogod.video.meetingapi.aws.sns.SMSSendingService;
import com.doogod.video.meetingapi.db.exceptions.RelativeNotFoundException;
import com.doogod.video.meetingapi.db.exceptions.ResidentNotFoundException;
import com.doogod.video.meetingapi.db.models.Relative;
import com.doogod.video.meetingapi.db.models.Resident;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelativeService {

    @Autowired
    Jdbi jdbi;

    @Autowired
    SMSSendingService smsSendingService;

    public void insert(Relative relative, Resident resident) throws ResidentNotFoundException {
        var id = jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO relatives(name, phone, resident_id) values (:name, :phone, :resident_id);")
                .bind("name", relative.getName())
                .bind("phone", relative.getPhone())
                .bind("resident_id", resident.getId())
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Long.class)
                .one()
        );

        relative.setResidentId(resident.getId());
        relative.setId(id.intValue());
        smsSendingService.sendTextMessage(relative.getPhone(), "Hello " + relative.getName() + "! You've been added as a relative to " + resident.getName());
    }

    public List<Relative> findByResident(Resident resident) {
        List<Relative> relatives = jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM relatives WHERE resident_id = :resident_id;")
                .bind("resident_id", resident.getId())
                .registerRowMapper(ConstructorMapper.factory(Relative.class))
                .mapTo(Relative.class)
                .list()
            );
        return relatives;

    }

    public Relative findById(Integer id) throws RelativeNotFoundException{
        try {
            return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM relatives WHERE id = :id;")
                    .bind("id", id)
                    .registerRowMapper(ConstructorMapper.factory(Relative.class))
                    .mapTo(Relative.class)
                    .one()
            );
        } catch(IllegalStateException e) {
            throw new RelativeNotFoundException();
        }
    }

    public void delete(Relative relative) {
        jdbi.withHandle(handle -> handle.createUpdate("DELETE FROM relatives WHERE id = :id")
                .bind("id", relative.getId())
                .execute()
        );
    }
}
