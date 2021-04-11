package com.doogod.video.meetingapi.db.services;

import com.doogod.video.meetingapi.db.models.Admin;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    Jdbi jdbi;

    public void insert(Admin admin) {
        var id = jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO admins(name) values (:name);")
                .bind("name", admin.getName())
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Long.class)
                .one()
        );
        admin.setId(id.intValue());
    }

    public void delete(Admin admin) {
        jdbi.withHandle(handle -> handle.createUpdate("DELETE FROM admins WHERE id = :id")
                .bind("id", admin.getId())
                .execute()
        );
    }
}
