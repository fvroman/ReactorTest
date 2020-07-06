package example.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AsyncControllerForTest {
    @GetMapping(value = "/async-flux", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<String> getAsync() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(String.valueOf(i));
        }
        return Flux.fromIterable(list).delayElements(Duration.of(1, ChronoUnit.SECONDS));
    }
}
