package flextrade.flexvision.fx.audit.json;

import java.util.Date;

import com.google.common.annotations.VisibleForTesting;
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

    public String convertToEmailMessage(){
        StringBuilder builder = new StringBuilder();
        builder.append("AuditLogQuery(Admin User=");
        builder.append(maxxUser);
        builder.append(", startDate=");
        builder.append(startDate.toString());
        builder.append(", endDate=");
        builder.append(endDate.toString());
        builder.append(", replyTo=");
        builder.append(replyTo);
        if(limit != null){
            builder.append(", limit=");
            builder.append(limit);
        }
        builder.append(")");
        return builder.toString();
    }
}
