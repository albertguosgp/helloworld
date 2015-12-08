package flextrade.flexvision.fx;

import flextrade.flexvision.fx.alert.pojo.Alert;
import flextrade.flexvision.fx.alert.pojo.AlertPriceSide;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AlertTest {
    @Test
    public void shouldImplementProperHashEqual(){
        Alert alert1 = new Alert();
        alert1.setId(1234L);
        alert1.setPrice("1.32454");
        alert1.setMessage("Purchase 1M USD/SGD");
        alert1.setSide(AlertPriceSide.ASK);

        Alert alert2 = new Alert();
        alert2.setId(1234L);
        alert2.setPrice("1.32454");
        alert2.setMessage("Purchase 1M USD/SGD");
        alert2.setSide(AlertPriceSide.ASK);
        assertEquals(alert1, alert2);
    }

    @Test
    public void shouldImplementProperHashNotEqual(){
        Alert alert1 = new Alert();
        alert1.setId(1L);
        Alert alert2 = new Alert();
        alert2.setId(2L);
        assertNotEquals(alert1, alert2);
    }
}
