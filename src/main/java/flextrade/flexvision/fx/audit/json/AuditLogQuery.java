package flextrade.flexvision.fx.audit.json;

import java.util.Date;

import lombok.Data;

@Data
public class AuditLogQuery {
    private String maxxUser;
    private Date startDate;
    private Date endDate;
    private String replyTo;
    private Integer limit;

    public static AuditLogQuery of(String maxxUser, Date startDate, Date endDate, String replyTo, Integer limit) {
        AuditLogQuery auditLogQuery = new AuditLogQuery();
        auditLogQuery.setMaxxUser(maxxUser);
        auditLogQuery.setStartDate(startDate);
        auditLogQuery.setEndDate(endDate);
        auditLogQuery.setReplyTo(replyTo);
        auditLogQuery.setLimit(limit);

        return auditLogQuery;
    }
}
