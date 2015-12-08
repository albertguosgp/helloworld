package flextrade.flexvision.fx.alert.dao;

import flextrade.flexvision.fx.alert.json.AlertQuery;
import flextrade.flexvision.fx.alert.pojo.Alert;

import java.util.List;

public interface AlertDao {
    void save(Alert alert);

    void delete(Long id);

    List<Alert> findHistoryAlert();

    List<Alert> findAlert(AlertQuery alertQuery);
}
