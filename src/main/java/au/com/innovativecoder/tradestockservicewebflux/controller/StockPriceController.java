package au.com.innovativecoder.tradestockservicewebflux.controller;

import au.com.innovativecoder.tradestockservicewebflux.dto.StockPrice;
import au.com.innovativecoder.tradestockservicewebflux.dto.TickerPrice;
import au.com.innovativecoder.tradestockservicewebflux.service.StockPriceService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for handling stock price requests.
 */
@RestController
@RequestMapping("/stock")
public class StockPriceController {

    private final StockPriceService stockPriceService;

    /**
     * Constructor for StockPriceController.
     *
     * @param stockPriceService the service to handle stock price operations
     */
    public StockPriceController(StockPriceService stockPriceService) {
        this.stockPriceService = stockPriceService;
    }

    /**
     * Endpoint to get the current price of a specific stock ticker.
     *
     * @param ticker the stock ticker
     * @return a Mono emitting the current TickerPrice
     */
    @GetMapping("/{ticker}")
    public Mono<TickerPrice> getCurrentPrice(@PathVariable String ticker) {
        return stockPriceService.getCurrentPrice(ticker).map(price -> new TickerPrice(ticker, price.getPrice()));
    }

    /**
     * Endpoint to get a continuous stream of stock prices.
     *
     * @return a Flux emitting StockPrice objects at regular intervals
     */
    @GetMapping(value = "/price-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StockPrice> getPriceStream() {
        return stockPriceService.getPriceStream();
    }
}
