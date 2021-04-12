package com.doogod.video.meetingapi.db.services;

import com.doogod.video.meetingapi.db.exceptions.IdentityNotFoundException;
import com.doogod.video.meetingapi.db.exceptions.DatabaseException;
import com.doogod.video.meetingapi.db.exceptions.UnkownIdentityTypeException;
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
        switch (identity.getType()) {
            case "admin":
                insertAdmin(identity);
                break;
            case "device":
                insertDevice(identity);
                break;
            case "resident":
                insertResident(identity);
                break;
            default:
                throw new UnkownIdentityTypeException();
        }
    }

    private void insertAdmin(Identity identity) throws DatabaseException {
        jdbi.withHandle(handle -> {
            try {
                return handle.createUpdate("INSERT INTO identities(type, username, password, admin_id, residency_id) values (:type, :username, :password, :admin_id, :residency_id);")
                        .bind("type", identity.getType())
                        .bind("username", identity.getUsername())
                        .bind("password", bcrypt.encode(identity.getPassword()))
                        .bind("admin_id", identity.getAdminId())
                        .bind("residency_id", identity.getResidencyId())
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

    private void insertDevice(Identity identity) throws DatabaseException {
        jdbi.withHandle(handle -> {
            try {
                return handle.createUpdate("INSERT INTO identities(type, username, device_id, residency_id) values (:type, :username, :device_id, :residency_id);")
                        .bind("type", identity.getType())
                        .bind("username", identity.getUsername())
                        .bind("device_id", identity.getDeviceId())
                        .bind("residency_id", identity.getResidencyId())
                        .registerRowMapper(ConstructorMapper.factory(Identity.class))
                        .executeAndReturnGeneratedKeys()
                        .mapTo(Identity.class)
                        .one();
            } catch (UnableToExecuteStatementException e){
                throw new DatabaseException();
            }
        });
    }

    private void insertResident(Identity identity) throws DatabaseException {
        jdbi.withHandle(handle -> {
            try {
                return handle.createUpdate("INSERT INTO identities(type, username, resident_id, residency_id) values (:type, :username, :resident_id, :residency_id);")
                        .bind("type", identity.getType())
                        .bind("username", identity.getUsername())
                        .bind("resident_id", identity.getResidentId())
                        .bind("residency_id", identity.getResidencyId())
                        .registerRowMapper(ConstructorMapper.factory(Identity.class))
                        .executeAndReturnGeneratedKeys()
                        .mapTo(Identity.class)
                        .one();
            } catch (UnableToExecuteStatementException e){
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
