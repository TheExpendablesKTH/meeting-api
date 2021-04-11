package com.doogod.video.meetingapi.db.services;

import com.doogod.video.meetingapi.db.exceptions.IdentityNotFoundException;
import com.doogod.video.meetingapi.db.exceptions.DatabaseException;
import com.doogod.video.meetingapi.db.exceptions.UsernameNotUniqueException;
import com.doogod.video.meetingapi.db.models.Identity;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class IdentityService {

    @Autowired
    Jdbi jdbi;

    @Autowired
    BCryptPasswordEncoder bcrypt;

    public void insert(Identity identity) throws DatabaseException {
        jdbi.withHandle(handle -> {
            try {
                return handle.createUpdate("INSERT INTO identities(username, password, admin_id) values (:username, :password, :admin_id);")
                    .bind("username", identity.getUsername())
                    .bind("password", bcrypt.encode(identity.getPassword()))
                    .bind("admin_id", identity.getAdminId())
                    .registerRowMapper(ConstructorMapper.factory(Identity.class))
                    .executeAndReturnGeneratedKeys()
                    .mapTo(Identity.class)
                    .one();
            } catch (UnableToExecuteStatementException e){
                if (e.getMessage().contains("unique")) {
                    throw new UsernameNotUniqueException();
                }
                throw new DatabaseException();
            }
        });
    }

    public Identity findByUsername(final String username) throws IdentityNotFoundException {
        try {
            return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM identities WHERE username = :username;")
                    .bind("username", username)
                    .registerRowMapper(ConstructorMapper.factory(Identity.class))
                    .mapTo(Identity.class)
                    .one()
            );
        } catch (IllegalStateException e) {
            throw new IdentityNotFoundException();
        }
    }
}
