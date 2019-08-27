package com.example.oauthfb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "tokens")
public class TokenTable {
    @Id
    private int id;
    private String accessToken;
    private String userId;
}
