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

@RestController
@RequestMapping("/stock")
public class StockPriceController {

    private final StockPriceService stockPriceService;

    public StockPriceController(StockPriceService stockPriceService) {
        this.stockPriceService = stockPriceService;
    }

    @GetMapping("/{ticker}")
    public Mono<TickerPrice> getCurrentPrice(@PathVariable String ticker) {
        return stockPriceService.getCurrentPrice(ticker).map(price -> new TickerPrice(ticker, price));
    }


    @GetMapping(value = "/price-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StockPrice> getPriceStream() {
        return stockPriceService.getPriceStream();
    }
}

