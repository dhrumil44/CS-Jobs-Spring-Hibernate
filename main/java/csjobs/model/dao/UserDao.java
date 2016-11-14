package csjobs.model.dao;

import java.util.List;

import csjobs.model.User;

public interface UserDao {

    User getUser( Long id );

    User getUser( String email );

    List<User> getUsers( String role );

    User saveUser( User user );

}
