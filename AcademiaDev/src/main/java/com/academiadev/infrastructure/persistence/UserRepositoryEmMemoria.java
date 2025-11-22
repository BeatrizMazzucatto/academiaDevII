package com.academiadev.infrastructure.persistence;

import com.academiadev.application.repositories.UserRepository;
import com.academiadev.domain.entities.Student;
import com.academiadev.domain.entities.User;

import java.util.*;
import java.util.stream.Collectors;

public class UserRepositoryEmMemoria implements UserRepository {
    private final Map<String, User> users;
    
    public UserRepositoryEmMemoria() {
        this.users = new HashMap<>();
    }
    
    @Override
    public void save(User user) {
        users.put(user.getEmail(), user);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(users.get(email));
    }
    
    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
    
    @Override
    public List<User> findAllStudents() {
        return users.values()
            .stream()
            .filter(user -> user instanceof Student)
            .collect(Collectors.toList());
    }
}

