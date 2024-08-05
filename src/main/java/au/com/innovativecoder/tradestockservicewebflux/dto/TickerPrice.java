package au.com.innovativecoder.tradestockservicewebflux.dto;

public class TickerPrice {
    private final String ticker;
    private final int price;

    public TickerPrice(String ticker, int price) {
        this.ticker = ticker;
        this.price = price;
    }

    public String getTicker() {
        return ticker;
    }

    public int getPrice() {
        return price;
    }

}
