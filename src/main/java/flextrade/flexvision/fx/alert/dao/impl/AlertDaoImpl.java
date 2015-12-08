package flextrade.flexvision.fx.alert.dao.impl;

import flextrade.flexvision.fx.alert.dao.AlertDao;
import flextrade.flexvision.fx.alert.json.AlertQuery;
import flextrade.flexvision.fx.alert.pojo.Alert;
import flextrade.flexvision.fx.alert.pojo.AlertStatus;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static flextrade.flexvision.fx.utils.CollectionUtils.toNotNullList;
import static org.springframework.util.StringUtils.isEmpty;
import static org.springframework.util.StringUtils.trimWhitespace;

@Component @Slf4j public class AlertDaoImpl implements AlertDao {

    @Autowired private SessionFactory sessionFactory;

    @Override public void save(Alert alert) {
        log.debug("Saving alert {}", alert);
        sessionFactory.getCurrentSession().saveOrUpdate(alert);
    }

    @Override public void delete(Long id) {
        log.debug("Removing alert {}- set status to DELETED", id);
        Session session = sessionFactory.getCurrentSession();
        Alert alert = (Alert) session.load(Alert.class, id);
        alert.setStatus(AlertStatus.DELETED);
        alert.setDeleted(new Date());
        session.save(alert);
    }

    @Override public List<Alert> findHistoryAlert() {
        log.debug("Finding all alerts");
        Criteria criteria = createCriteria();
        List<Alert> allAlerts = criteria.list();
        return toNotNullList(allAlerts);
    }

    @Override public List<Alert> findAlert(AlertQuery alertQuery) {
        log.debug("Finding alert with query {}", alertQuery);
        Criteria criteria = createCriteria();
        Conjunction conjunction = Restrictions.conjunction();

        if (!isEmpty(alertQuery.getMaxxUser())) {
            conjunction.add(Restrictions.eq("maxxUser", trimWhitespace(alertQuery.getMaxxUser())));
        }
        conjunction.add(Restrictions.ne("status", AlertStatus.DELETED));

        criteria.add(conjunction);
        return toNotNullList(criteria.list());
    }

    private Criteria createCriteria() {
        return sessionFactory.getCurrentSession().createCriteria(Alert.class);
    }
}
