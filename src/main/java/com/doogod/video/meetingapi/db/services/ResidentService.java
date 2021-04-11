package com.doogod.video.meetingapi.db.services;

import com.doogod.video.meetingapi.db.models.Resident;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResidentService {

    @Autowired
    Jdbi jdbi;

    public void insert(Resident resident) {
        var id = jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO residents(name, residency_id) values (:name, :residency_id);")
                .bind("name", resident.getName())
                .bind("residency_id", resident.getResidencyId())
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Long.class)
                .one()
        );
        resident.setId(id.intValue());
    }

    public List<Resident> all() {
        List<Resident> residents = jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM residents;")
                .registerRowMapper(ConstructorMapper.factory(Resident.class))
                .mapTo(Resident.class)
                .list()
        );
        return residents;
    }

    public List<Resident> findByResidencyId(Integer residencyId) {
        List<Resident> residents = jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM residents WHERE residency_id = :residency_id;")
                .bind("residency_id", residencyId)
                .registerRowMapper(ConstructorMapper.factory(Resident.class))
                .mapTo(Resident.class)
                .list()
        );
        return residents;
    }

    public void delete(Resident resident) {
        jdbi.withHandle(handle -> handle.createUpdate("DELETE FROM residents WHERE id = :id")
                .bind("id", resident.getId())
                .execute()
        );
    }
}
