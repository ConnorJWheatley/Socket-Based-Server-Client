package StockServer;

public class Trader {
    private final long traderID;
    private int hasStock;

    public Trader(long traderID){
        this.traderID = traderID;
    }

    public long getTraderID() { return traderID; }

    public int getHasStock() { return hasStock; }

    public void setHasStock(int setHasStock) { hasStock = setHasStock; }

}
