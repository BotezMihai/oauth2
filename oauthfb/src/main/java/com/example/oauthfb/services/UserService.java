package com.example.oauthfb.services;

import com.example.oauthfb.entity.User;
import com.example.oauthfb.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean exists(String id) {
        return userRepository.findById(id).isPresent();
    }

    public void insertUser(User user) {
        this.userRepository.save(user);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

}
