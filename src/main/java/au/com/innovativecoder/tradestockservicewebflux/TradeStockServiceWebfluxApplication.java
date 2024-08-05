package au.com.innovativecoder.tradestockservicewebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableScheduling
public class TradeStockServiceWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeStockServiceWebfluxApplication.class, args);
    }

}
