package com.example.oauthfb.interfaces;

import com.example.oauthfb.entity.UserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserDetails, String> {

}