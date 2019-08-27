package com.example.oauthfb.interfaces;

import com.example.oauthfb.entity.TokenTable;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.persistence.Id;

public interface TokenRepository extends MongoRepository<TokenTable, Integer> {
 boolean existsTokenTableByAccessToken(String accessToken);
 TokenTable findTokenTableByAccessToken (String id);
}
