package flextrade.flexvision.fx.alert.service;

import flextrade.flexvision.fx.alert.dao.AlertDao;
import flextrade.flexvision.fx.alert.json.AlertQuery;
import flextrade.flexvision.fx.alert.pojo.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service public class AlertService {

    @Autowired private AlertDao alertDao;

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Alert> get(AlertQuery alertQuery) {
        return alertDao.findAlert(alertQuery);
    }

    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public Alert save(Alert alert) {
        alertDao.save(alert);
        return alert;
    }

    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void delete(Long alertId) {
        alertDao.delete(alertId);
    }

    @Transactional public List<Alert> findAll() {
        return alertDao.findHistoryAlert();
    }
}
