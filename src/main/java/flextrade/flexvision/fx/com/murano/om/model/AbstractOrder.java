package flextrade.flexvision.fx.com.murano.om.model;

/**
 * Created by chenguo on 22/12/15.
 */
public abstract class AbstractOrder implements Order {
    private OrderState orderState;

    @Override
    public OrderId getId() {
        return null;
    }

    @Override
    public Symbol getSymbol() {
        return null;
    }

    @Override
    public Initiator getInitiator() {
        return null;
    }

    @Override
    public Acceptor getAcceptor() {
        return null;
    }

    @Override
    public OrderState getOrderState() {
        return orderState;
    }

    @Override
    public void setOrderState(OrderState orderState) {

    }

    @Override
    public void handleEvent(OrderEvent orderEvent) {

    }
}
