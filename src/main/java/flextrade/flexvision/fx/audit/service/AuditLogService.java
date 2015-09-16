package flextrade.flexvision.fx.audit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import flextrade.flexvision.fx.audit.dao.AuditLogDao;
import flextrade.flexvision.fx.audit.json.AuditLogQuery;
import flextrade.flexvision.fx.audit.pojo.AuditLog;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogDao auditLogDao;

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AuditLog> get(AuditLogQuery auditLogQuery) {
        return auditLogDao.findAuditLog(auditLogQuery);
    }

    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public Long save(AuditLog auditLog) {
        auditLogDao.save(auditLog);
        return auditLog.getId();
    }

    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void delete(Long auditLogId) {
        auditLogDao.delete(auditLogId);
    }

    @Transactional
    public List<AuditLog> findAll() {
        return auditLogDao.findAll();
    }
}
