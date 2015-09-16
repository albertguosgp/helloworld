package flextrade.flexvision.fx.audit.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import flextrade.flexvision.fx.audit.dao.AuditLogDao;
import flextrade.flexvision.fx.audit.json.AuditLogQuery;
import flextrade.flexvision.fx.audit.pojo.AuditLog;
import lombok.extern.slf4j.Slf4j;

import static flextrade.flexvision.fx.utils.CollectionUtils.toNotNullList;
import static org.springframework.util.StringUtils.isEmpty;

@Repository
@Slf4j
public class AuditLogDaoImpl implements AuditLogDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(AuditLog auditLog) {
        log.debug("Saving audit log {}", auditLog);
        Long identifier = (Long) sessionFactory.getCurrentSession().save(auditLog);
        auditLog.setId(identifier);
    }

    @Override
    public List<AuditLog> findAll() {
        Criteria criteria = createCriteria();
        List<AuditLog> allAuditLogs = criteria.list();

        return toNotNullList(allAuditLogs);
    }

    @Override
    public List<AuditLog> findAuditLog(AuditLogQuery auditLogQuery) {
        log.debug("Finding audit log by query {}", auditLogQuery);
        Criteria criteria = createCriteria();
        Conjunction conjunction = Restrictions.conjunction();
        if (!isEmpty(auditLogQuery.getMaxxUser())) {
            conjunction.add(Restrictions.like("maxxUser", auditLogQuery.getMaxxUser()));
        }
        if (auditLogQuery.getStartDate() != null) {
            conjunction.add(Restrictions.gt("auditDate", auditLogQuery.getStartDate()));
        }
        if (auditLogQuery.getEndDate() != null) {
            conjunction.add(Restrictions.le("auditDate", auditLogQuery.getEndDate()));
        }
        criteria.add(conjunction);

        return toNotNullList(criteria.list());
    }

    private Criteria createCriteria() {
        return sessionFactory.getCurrentSession().createCriteria(AuditLog.class);
    }
}
