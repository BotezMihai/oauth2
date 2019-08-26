package com.example.oauthfb.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Setter
@Getter
@Document(collection = "counters")
public class Counter {
    @Id
    private String id;
    private int seq;

}
