package com.example.oauthfb.interfaces;
import com.example.oauthfb.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

}