package StockServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StockMarket {
    private final Map<Long, Trader> traders;
    private long traderWithStock;

    public StockMarket(){
        traders = new TreeMap<>();
    }

    public void addTrader(long traderID, int hasStock){
        Trader trader = new Trader(traderID);
        if(hasStock == 1){
            trader.setHasStock(1);
        }
        else {
            trader.setHasStock(0);
        }
        traders.put(traderID, trader);
    }

    public void removeTrader(long traderID){ traders.remove(traderID); }

    public List<Long> getListOfTraders() { return new ArrayList<>(traders.keySet()); }

    public Trader getTrader(long traderID) { return traders.get(traderID); }

    public long traderWithStock(){
        for(Map.Entry<Long, Trader> entry : traders.entrySet()){
            if(entry.getValue().getHasStock() == 1){
                traderWithStock = entry.getKey();
            }
        }
        return traderWithStock;
    }

    public synchronized boolean moveStock(long fromTraderID, long toTraderID) {
        synchronized (traders){
            // need to check if person they are trying to send stock to is even connected
            Trader fromTrader = traders.get(fromTraderID);
            if(fromTrader.getHasStock() == 0){
                return false;
            }
            else {
                Trader toTrader = traders.get(toTraderID);
                fromTrader.setHasStock(0);
                toTrader.setHasStock(1);
                return true;
            }
        }
    }
}
