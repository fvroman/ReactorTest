package example.controller;

import example.model.UserModel;
import example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatabaseClient databaseClient;

    public UserController() {

    }

    @GetMapping(value = "/user/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<UserModel> getAllUsers() {
        return userRepository.findAll().log();
    }

    @GetMapping("/user/{id}")
    public Mono<UserModel> getUser(@PathVariable Long id) {
        return userRepository.findById(id).log();
    }

    @PostMapping("/user/add")
    public Mono<UserModel> addUser(@RequestBody UserModel user) {
        return userRepository.save(user).log();
    }

    @PostMapping(value = "user/addMultiple", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public Flux<UserModel> addMultiple() {
        return databaseClient.execute("insert into myUsers (name) values ('IAmUserFromDbClient1');" +
                "insert into myUsers (name) values ('IAmUserFromDbClient2');").as(UserModel.class).fetch().all();
    }

}
