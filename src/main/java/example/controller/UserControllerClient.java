package example.controller;

import example.model.UserModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

@RestController
public class UserControllerClient {
    @RequestMapping("/")
    public String index() {
        return "<div><a href=\"addRandom\"> Новый пользователь </a></div><br>" +
                "<div><a href=\"user/all\"> Все пользователи </a></div><br>" +
                "<div><a href=\"/allLong\"> Все пользователи с длинными именами </a></div><br>";
    }

    private static final String[] namesForTest = {"John", "Son", "Don", "Won", "Ron", "Elon"};

    @RequestMapping("/addRandom")
    public String addUser() {
        UserModel randomUser = new UserModel();
        randomUser.setName(namesForTest[new Random().nextInt(namesForTest.length)]);
        WebClient webClient = WebClient.create("http://localhost:8080/user/add");
        Mono<UserModel> userModelMono = webClient.post().bodyValue(randomUser).retrieve().bodyToMono(UserModel.class);
        System.out.println(userModelMono);
        //Должна придти модель с айдишником, занесенным в базу
        userModelMono.subscribe((user) -> System.out.println("Thread" + Thread.currentThread().getName() + " " + user),
                error -> System.out.println(error.getMessage()));
        System.out.println("after subscribe with thread" + Thread.currentThread().getName());
        // На самом деле может быть и не создан, т.к может возникнуть ошибка в Thread'е, который будет в subscribe
        return "Пользователь создан! "
                + "<div><a href=\"/\"> В начало </a></div><br>" +
                "<div><a href=\"/user/all\"> Все пользователи </a></div><br>";
    }

    @RequestMapping("/allLong")
    public Flux<UserModel> getAllUsersWithLongNames() {
        WebClient webClient = WebClient.create("http://localhost:8080/user/all");
        return webClient.get().retrieve().bodyToFlux(UserModel.class).filter(userModel -> userModel.getName().length() > 3).log();
    }

}
