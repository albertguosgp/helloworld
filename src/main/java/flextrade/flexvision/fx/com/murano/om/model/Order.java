package flextrade.flexvision.fx.com.murano.om.model;

public interface Order {
    OrderId getId();

    Symbol getSymbol();

    Initiator getInitiator();

    Acceptor getAcceptor();

    OrderState getOrderState();
}
