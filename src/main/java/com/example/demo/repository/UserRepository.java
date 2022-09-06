package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(User user) {
        mongoTemplate.insert(user);
    }

    public User findOneUser(String username) {
        Query query = Query.query(
                Criteria.where("username").is(username)
        );
        return mongoTemplate.findOne(query, User.class);
    }
}
