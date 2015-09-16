package flextrade.flexvision.fx.audit.dao;

import java.util.List;

import flextrade.flexvision.fx.audit.json.AuditLogQuery;
import flextrade.flexvision.fx.audit.pojo.AuditLog;

public interface AuditLogDao {
    void save(AuditLog auditLog);

    List<AuditLog> findAll();

    List<AuditLog> findAuditLog(AuditLogQuery auditLogQuery);
}
