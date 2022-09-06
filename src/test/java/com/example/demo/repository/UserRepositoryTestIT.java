package com.example.demo.repository;

import com.example.demo.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("integration-test")
public class UserRepositoryTestIT {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    public void tearDown() {
        mongoTemplate.dropCollection(User.class);
    }

    @Test
    public void findOneUser_by_using_username() {
        // given
        String givenUsername = "kai";
        String givenPassword = "12341234";
        mongoTemplate.insert(new User(givenUsername, givenPassword));

        // when
        User user = userRepository.findOneUser(givenUsername);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(givenUsername);
        assertThat(user.getPassword()).isEqualTo(givenPassword);
    }

    @Test
    public void insertUser() {
        // given
        User givenUser = new User("kai", "1234");

        // when
        userRepository.insert(givenUser);

        // then
        User user = mongoTemplate
                .findOne(Query.query(Criteria.where("username").is("kai")), User.class);

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("kai");
        assertThat(user.getPassword()).isEqualTo("1234");
    }
}
