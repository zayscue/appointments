package edu.wgu.c195.appointments.application;

import edu.wgu.c195.appointments.domain.entities.User;
import edu.wgu.c195.appointments.persistence.repositories.IRepository;
import edu.wgu.c195.appointments.persistence.repositories.UserRepository;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

public class UserManager {

    private final IRepository<User> users;

    public UserManager() {
        this.users = new UserRepository();
    }

    public UserManager(IRepository<User> users) {
        this.users = users;
    }

    public User findById(int id) {
        final User user;
        try {
            user = this.users.get(id);
        } catch (SQLException e) {
            return null;
        }
        return user;
    }

    public User findByUserName(String userName) throws NullPointerException {
        if(userName == null)
            throw new NullPointerException("userName");
        Optional<User> user = this.users.getAll()
                .filter(u -> u.getUserName().equals(userName))
                .findFirst();
        if(user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }

    public boolean create(String userName, String password, String createdBy) throws NullPointerException {
        if(userName == null)
            throw new NullPointerException("user");
        if(password == null)
            throw new NullPointerException("password");
        if(createdBy == null)
            throw new NullPointerException("createdBy");
        try {
            User user = new User();
            Instant instant = Instant.now();
            Date date = new Date(instant.toEpochMilli());
            byte active = 1;
            user.setUserName(userName);
            user.setPassword(password);
            user.setActive(active);
            user.setCreateDate(date);
            user.setCreateBy(createdBy);
            user.setLastUpdatedBy(createdBy);
            user.setLastUpdate(Timestamp.from(instant));
            this.users.add(user);
            this.users.save();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean checkPassword(User user, String password) throws NullPointerException {
        if(password == null)
            throw new NullPointerException("password");
        if(user == null)
            throw new NullPointerException("user");
        if(user.getPassword() == null)
            throw new NullPointerException("user.password");
        return user.getPassword().equals(password);
    }
}
