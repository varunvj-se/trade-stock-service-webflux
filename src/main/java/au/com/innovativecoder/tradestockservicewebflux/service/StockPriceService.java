package au.com.innovativecoder.tradestockservicewebflux.service;

import au.com.innovativecoder.tradestockservicewebflux.dto.StockPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service class for handling stock price operations.
 */
@Service
public class StockPriceService {

    // Logger instance for logging events
    private static final Logger log = LoggerFactory.getLogger(StockPriceService.class);

    // List of example stock tickers
    private static final List<String> TICKERS = List.of("APPLE", "GOOGLE", "AMAZON", "BABATATA");

    // Map to store the latest stock prices for each ticker
    private final Map<String, StockPrice> latestPrices = new ConcurrentHashMap<>();

    // Sink to emit and replay the latest stock prices
    private final Sinks.Many<StockPrice> priceSink = Sinks.many().replay().latest();

    /**
     * Constructor for StockPriceService.
     * Initializes the price update process to run every 2 seconds.
     */
    public StockPriceService() {
        // Create a Flux that emits an event every 2 seconds
        Flux.interval(Duration.ofSeconds(2))
                // On each event, update the stock prices
                .doOnNext(this::updatePrices)
                // Subscribe to start the interval process
                .subscribe();
    }

    /**
     * Updates the prices for all tickers and emits the new prices.
     *
     * @param interval the current interval (not used in the method)
     */
    private void updatePrices(long interval) {
        // Iterate over each ticker in the TICKERS list
        TICKERS.forEach(ticker -> {
            // Generate a mock price for the current ticker
            int price = getMockPrice();
            // Create a new StockPrice object with the current ticker, generated price, and current timestamp
            StockPrice stockPrice = new StockPrice(ticker, price, LocalDateTime.now().toString());
            // Update the latestPrices map with the new StockPrice object for the current ticker
            latestPrices.put(ticker, stockPrice);
            // Emit the new StockPrice object to the priceSink
            priceSink.tryEmitNext(stockPrice);
        });
    }

    /**
     * Retrieves the current price of a stock for a specific ticker.
     *
     * @param ticker the stock ticker
     * @return a Mono emitting the current StockPrice
     */
    public Mono<StockPrice> getCurrentPrice(String ticker) {
        // Return the latest price for the given ticker as a Mono
        return Mono.justOrEmpty(latestPrices.get(ticker));
    }

    /**
     * Provides a continuous stream of stock prices.
     *
     * @return a Flux emitting StockPrice objects
     */
    public Flux<StockPrice> getPriceStream() {
        // Return the Flux that emits the latest stock prices
        return priceSink.asFlux();
    }

    /**
     * Generates a mock stock price between 80 and 100.
     *
     * @return a random stock price as an Integer
     */
    private int getMockPrice() {
        // Generate a random price between 80 and 100
        return 80 + (int) (Math.random() * 21);
    }
}


/**
 * Explanation of Key Concepts
 * Sinks.Many and emit
 * Sink: A Sink is a special type in Reactor that allows you to programmatically push data into a reactive stream. Think of it as a way to manually control the flow of data into a Flux or Mono.
 * Sinks.Many: This type of sink can emit multiple items. In this code, Sinks.many().replay().latest() is used to create a sink that replays the latest emitted item to any new subscribers.
 * emit: The tryEmitNext method is used to push the next item into the sink. If the emission is successful, the item is passed down the stream to subscribers.
 */
