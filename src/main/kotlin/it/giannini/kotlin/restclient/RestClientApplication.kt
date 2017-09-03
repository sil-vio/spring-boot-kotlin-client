package it.giannini.kotlin.restclient

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux

@SpringBootApplication
class RestClientApplication {

    private val log = LoggerFactory.getLogger(RestClientApplication::class.java)


    @Bean
    internal fun webClient(): WebClient {
        return WebClient.create("http://localhost:8080/");
    }

    @Bean
    internal fun demo(client: WebClient) = CommandLineRunner {
        log.info("Runnnno")


        client.post().uri("/transaction")
                .body(BodyInserters.fromObject<Any>(Transaction(null, 22L, null)))
                .exchange()
                .subscribe()


        client.get()
                .uri("/events")
                .retrieve()
                .bodyToFlux<Transaction>(Transaction::class.java)
                .doOnComplete {log.info("completato 1")}
//                .flatMap({ t1 ->
//                    client.get()
//                            .uri("/events")
//                            .retrieve()
//                            .bodyToFlux<Transaction>()
//                })
                .subscribe({ t2 -> log.info("e:" + t2.toString()) })

        client.get()
                .uri("/transaction")
                .retrieve()
                .bodyToFlux<Transaction>()
                .doOnComplete { log.info("completato 2")}
                .subscribe({t -> log.info("t:" + t.toString())})



        client.post().uri("/transaction")
                .body(BodyInserters.fromObject<Any>(Transaction(null, 25L, null)))
                .exchange()
                .subscribe()


    }

}

fun main(args: Array<String>) {
    SpringApplication.run(RestClientApplication::class.java, *args)
}
