package be.drs.webclient.iface;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class ApiRepository {

    private final WebClient.Builder webClientBuilder;

    private final EurekaClient eurekaClient;

    public ApiRepository(WebClient.Builder webClientBuilder,
                         EurekaClient eurekaClient) {
        this.webClientBuilder = webClientBuilder;
        this.eurekaClient = eurekaClient;
    }

    public Mono<String> count() {
        return webClientBuilder.build()
                .get()
                .uri("http://api/api/count")
                .retrieve()
                .bodyToMono(String.class)
                .retry(2)  // if one api server is down the next one will be tried... if one of them is up this will succeed
                // however it seems the client side loadbalancer keeps the old server in memory, even when the eurekaserver removed it from the registry
                // only on restart is the servicelist refreshed, surely this can be refreshed automagically!!
                //-> see properties: eureka.client.healthcheck.enabled=true
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                        return Mono.just("Internal Server Error: " + ex.getMessage());
                    } else {
                        return Mono.error(ex);
                    }
                })
                .onErrorResume(Exception.class, ex -> Mono.just("An unexpected error occurred: " + ex.getMessage()));

    }

    public String serviceInfo(){
        final StringBuffer result = new StringBuffer();
        List<InstanceInfo> instances = eurekaClient.getApplication("api").getInstances();
        instances.forEach(instance -> {
            /*System.out.println("healt-check-url : " + instance.getHealthCheckUrl());
            System.out.println("status : " + instance.getStatus());
            System.out.println("status-url : " + instance.getStatusPageUrl());*/
            System.out.println("instance info : " + instance.toString());
            result.append("\n").append(instance.toString());
        });
        return result.toString();
    }
}
