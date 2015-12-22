package flextrade.flexvision.fx.com.murano.om.model.state;

import flextrade.flexvision.fx.com.murano.om.model.Order;
import flextrade.flexvision.fx.com.murano.om.model.OrderState;

public class BustedOrder implements OrderState {
    private Order order;

    @Override
    public void stateCheck() {
        order.setOrderState(new NewOrder());
    }
}
