package au.com.innovativecoder.tradestockservicewebflux.dto;

public class StockPrice {
    private final String ticker;
    private final int price;
    private final String time;

    public StockPrice(String ticker, int price, String time) {
        this.ticker = ticker;
        this.price = price;
        this.time = time;
    }

    public String getTicker() {
        return ticker;
    }

    public int getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }
}
