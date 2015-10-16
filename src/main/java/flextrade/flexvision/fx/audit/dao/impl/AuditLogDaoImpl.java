package flextrade.flexvision.fx.audit.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import flextrade.flexvision.fx.audit.dao.AuditLogDao;
import flextrade.flexvision.fx.audit.json.AuditLogQuery;
import flextrade.flexvision.fx.audit.pojo.AuditLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import static flextrade.flexvision.fx.utils.CollectionUtils.toNotNullList;
import static org.springframework.util.StringUtils.isEmpty;
import static org.springframework.util.StringUtils.trimWhitespace;

@Repository
@Slf4j
public class AuditLogDaoImpl implements AuditLogDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(AuditLog auditLog) {
        log.debug("Saving audit log {}", auditLog);
		Optional<AuditLog> existingAuditLog = findExistingAuditLog(auditLog);
		if (existingAuditLog.isPresent()) {
			log.debug("Found existing audit log {}, and won't save again", existingAuditLog);
			auditLog.setId(existingAuditLog.get().getId());
		} else {
			Long identifier = (Long) sessionFactory.getCurrentSession().save(auditLog);
			auditLog.setId(identifier);
		}

    }

	private Optional<AuditLog> findExistingAuditLog(AuditLog auditLog) {
		Criteria criteria = createCriteria();
		Conjunction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("maxxUser", auditLog.getMaxxUser()));
		conjunction.add(Restrictions.eq("auditDate", auditLog.getAuditDate()));
		conjunction.add(Restrictions.eq("operation", auditLog.getOperation()));

		criteria.add(conjunction);
		AuditLog auditLogFromDb = (AuditLog)criteria.uniqueResult();
		return auditLogFromDb == null ? Optional.empty() : Optional.of(auditLogFromDb);
	}

    @Override
    public void delete(Long id) {
        log.debug("Removing audit log by id {}", id);
        Session session = sessionFactory.getCurrentSession();
        AuditLog auditLogToBeDeleted = (AuditLog) session.load(AuditLog.class, id);
        session.delete(auditLogToBeDeleted);
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
            conjunction.add(Restrictions.ilike("maxxUser", trimWhitespace(auditLogQuery.getMaxxUser()), MatchMode.ANYWHERE));
        }
        if (auditLogQuery.getStartDate() != null) {
            conjunction.add(Restrictions.gt("auditDate", auditLogQuery.getStartDate()));
        }
        if (auditLogQuery.getEndDate() != null) {
            conjunction.add(Restrictions.le("auditDate", auditLogQuery.getEndDate()));
        }
        criteria.add(conjunction);
        if (auditLogQuery.getLimit() != null) {
            criteria.setMaxResults(auditLogQuery.getLimit());
        }

        criteria.addOrder(Order.desc("auditDate"));

        return toNotNullList(criteria.list());
    }

    private Criteria createCriteria() {
        return sessionFactory.getCurrentSession().createCriteria(AuditLog.class);
    }
}
