package flextrade.flexvision.fx.audit.pojo;

import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity(name = "audit_log")
public class AuditLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "serial")
    private Long id;

    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "maxx_user")
    private String maxxUser;

    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "operation")
    private String operation;

    @NotNull
    @Column(name = "audit_timestamp")
    @Type(type = "timestamp")
    private Date auditDate;

    @Size(max = 128)
    @Column(name = "remarks")
    private String remarks;

    public static AuditLog of(long id, String maxxUser, String operation, Date auditDate, String remarks) {
        AuditLog auditLog = new AuditLog();
        auditLog.setId(id);
        auditLog.setMaxxUser(maxxUser);
        auditLog.setOperation(operation);
        auditLog.setAuditDate(auditDate);
        auditLog.setRemarks(remarks);

        return auditLog;
    }
}
