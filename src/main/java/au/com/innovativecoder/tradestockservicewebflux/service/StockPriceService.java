package au.com.innovativecoder.tradestockservicewebflux.service;

import au.com.innovativecoder.tradestockservicewebflux.dto.StockPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for handling stock price operations.
 */
@Service
public class StockPriceService {

    // Logger instance for logging events
    Logger log = LoggerFactory.getLogger(StockPriceService.class);

    // List of example stock tickers
    List<String> tickers = List.of("APPLE", "GOOGLE", "AMAZON", "BABATATA");

    // Map to store shared Monos for each ticker
    private final Map<String, Mono<Integer>> sharedPrices = new HashMap<>();

    public StockPriceService() {
        // Initialize the sharedPrices map with a Mono for each ticker
        for (String ticker : tickers) {
            sharedPrices.put(ticker, Mono.defer(() -> Mono.just(getMockPrice()))
                    .cache(Duration.ofSeconds(2)));
        }
    }

    /**
     * Retrieves the current price of a stock for a specific ticker.
     *
     * @param ticker the stock ticker
     * @return a Mono emitting the current stock price as an Integer.
     */
    public Mono<Integer> getCurrentPrice(String ticker) {
        return sharedPrices.get(ticker);
    }

    /**
     * Provides a continuous stream of stock prices.
     *
     * @return a Flux emitting StockPrice objects at regular intervals.
     */
    public Flux<StockPrice> getPriceStream() {
        // Returns a Flux that emits StockPrice objects at regular intervals of 2 seconds.
        // Logs a message if the request is cancelled.
        return Flux.interval(Duration.ofSeconds(2))
                // For each interval, it iterates over the list of tickers and maps each ticker to a StockPrice object
                .flatMap(sequence -> Flux.fromIterable(tickers)
                // using the sharedPrice Mono to get the current price.
                .flatMap(ticker -> sharedPrices.get(ticker).map(
                        price -> new StockPrice(ticker, price, LocalDateTime.now().toString())
                )))
            .doOnCancel(() -> log.info("request cancelled for /stock/price-stream"));
    }

    /**
     * Generates a mock stock price between 90 and 100.
     *
     * @return a random stock price as an Integer.
     */
    private int getMockPrice() {
        return 90 + (int) (Math.random() * 11);
    }
}
