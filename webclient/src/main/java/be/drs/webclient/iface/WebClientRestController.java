package be.drs.webclient.iface;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class WebClientRestController {

    private final ApiRepository apiRepository;


    public WebClientRestController(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    @GetMapping("/client/count")
    public Mono<String> count() {
        return apiRepository.count();
    }

    @GetMapping("/serviceinfo")
    public String serviceinfo() {
        return apiRepository.serviceInfo();
    }
}
