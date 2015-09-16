package flextrade.flexvision.fx.audit.dao;

import java.util.List;

import flextrade.flexvision.fx.audit.json.AuditLogQuery;
import flextrade.flexvision.fx.audit.pojo.AuditLog;

public interface AuditLogDao {
    void save(AuditLog auditLog);

    void delete(Long id);

    List<AuditLog> findAll();

    List<AuditLog> findAuditLog(AuditLogQuery auditLogQuery);
}
