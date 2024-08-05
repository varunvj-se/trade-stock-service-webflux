package au.com.innovativecoder.tradestockservicewebflux.service;

import au.com.innovativecoder.tradestockservicewebflux.dto.StockPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for handling stock price operations.
 */
@Service
public class StockPriceService {

    // Logger instance for logging events
    Logger log = LoggerFactory.getLogger(StockPriceService.class);

    // List of example stock tickers
    List<String> tickers = List.of("APPLE", "GOOGLE", "AMAZON", "BABATATA");

    /**
     * Retrieves the current price of a stock.
     *
     * @return a Mono emitting the current stock price as an Integer.
     */
    public Mono<Integer> getCurrentPrice() {
        return Mono.just(getMockPrice());
    }

    /**
     * Provides a continuous stream of stock prices.
     *
     * @return a Flux emitting StockPrice objects at regular intervals.
     */
    public Flux<StockPrice> getPriceStream() {
        // Creates a Flux that emits long values starting from 0 at 2-second intervals
        return Flux.interval(Duration.ofSeconds(2))
            // For each emitted value, map the list of tickers to a Flux of StockPrice objects
            .flatMap(sequence -> Flux.fromIterable(tickers)
                // Create a new StockPrice object for each ticker with a mock price and the current timestamp
                .map(ticker -> new StockPrice(ticker, getMockPrice(), LocalDateTime.now().toString())))
            // Log a message when the request is cancelled
            .doOnCancel(() -> log.info("request cancelled for /stock/price-stream"));
    }

    /**
     * Generates a mock stock price between 100 and 130.
     *
     * @return a random stock price as an Integer.
     */
    private int getMockPrice() {
        return 90 + (int) (Math.random() * 11);
    }
}
