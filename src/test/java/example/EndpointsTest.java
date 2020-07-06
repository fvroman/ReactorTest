package example;

import example.model.UserModel;
import net.jodah.concurrentunit.Waiter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EndpointsTest {
    @Test
    public void addUser() throws Throwable {
        final WebClient client = WebClient.builder().baseUrl("http://localhost:8080").build();
        final Waiter waiter = new Waiter();
        UserModel userModel = new UserModel();
        userModel.setName("UserFromTest");
        Mono<UserModel> userModelMono = client.post().uri("user/add").bodyValue(userModel).retrieve().bodyToMono(UserModel.class);
        userModelMono.subscribe((user) -> {
            //Проверка ответа
            System.out.println(user);
            waiter.assertNotNull(user);
            waiter.assertEquals(user.getName(), "UserFromTest");
            //Проверка что пользователь появился в БД
            client.get().uri("user/" + user.getId()).retrieve().bodyToMono(UserModel.class).subscribe(userFromDb -> {
                waiter.assertNotNull(userFromDb);
                waiter.resume();
            });
        });
        waiter.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void addMultiple() throws Throwable {
        final WebClient client = WebClient.builder().baseUrl("http://localhost:8080").build();
        final Waiter waiter = new Waiter();
        Flux<UserModel> userModelFlux = client.post().uri("user/addMultiple").retrieve().bodyToFlux(UserModel.class);
        userModelFlux.subscribe(waiter::assertNotNull, System.out::print, waiter::resume);
        waiter.await(5, TimeUnit.SECONDS);
    }
}
