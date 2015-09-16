package flextrade.flexvision.fx.audit.json;

import java.util.Date;

import lombok.Data;

@Data
public class AuditLogQuery {
    private String maxxUser;
    private Date startDate;
    private Date endDate;
}
