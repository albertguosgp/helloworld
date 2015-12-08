package flextrade.flexvision.fx.alert.pojo;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data @Entity(name = "alert") public class Alert implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @Column(name = "id", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @NotNull @Column(name = "maxx_user") private String maxxUser;

    @NotNull @Column(name = "status") @Enumerated(EnumType.STRING) private AlertStatus status;

    @NotNull @Column(name = "symbol") private String symbol;

    @NotNull @Column(name = "side") @Enumerated(EnumType.STRING) private AlertPriceSide side;

    @NotNull @Column(name = "price") private String price;

    @NotNull @Column(name = "firm") private String firm;

    @NotNull @Column(name = "delivery") private String delivery;

    @Column(name = "message") private String message;

    @NotNull @Column(name = "create_timestamp") @Type(type = "timestamp") private Date created;

    @Column(name = "trigger_timestamp") @Type(type = "timestamp") private Date triggered;

    @Column(name = "delete_timestamp") @Type(type = "timestamp") private Date deleted;

    @Column(name = "expire_timestamp") @Type(type = "timestamp") private Date expired;

    public static Alert of(long id, String maxxUser, AlertStatus status, String symbol,
        AlertPriceSide side, String price, String firm, String delivery, String message,
        Date created, Date deleted, Date triggered, Date expired) {
        Alert alert = new Alert();
        alert.setCreated(created);
        alert.setDelivery(delivery);
        alert.setDeleted(deleted);
        alert.setExpired(expired);
        alert.setId(id);
        alert.setMaxxUser(maxxUser);
        alert.setFirm(firm);
        alert.setMessage(message);
        alert.setPrice(price);
        alert.setSymbol(symbol);
        alert.setSide(side);
        alert.setStatus(status);
        alert.setTriggered(triggered);

        return alert;
    }
}
