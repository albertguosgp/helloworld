package flextrade.flexvision.fx.alert.controller;

import flextrade.flexvision.fx.alert.json.AlertQuery;
import flextrade.flexvision.fx.alert.pojo.Alert;
import flextrade.flexvision.fx.alert.service.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;

@RestController @Slf4j public class AlertController {

    @Autowired private AlertService alertService;

    @RequestMapping(value = "/alert",
        method = RequestMethod.GET) public List<Alert> getAlert(AlertQuery alertQuery) {
        return alertService.get(alertQuery);
    }

    @RequestMapping(value = "/alert", method = RequestMethod.POST)
    public Alert saveAlert(@Valid @RequestBody Alert alert) {
        Alert savedAlert = alertService.save(alert);
        return savedAlert;
    }

    @RequestMapping(value = "/alert/{alertId}", method = RequestMethod.DELETE)
    public void deleteAlert(@PathVariable Long alertId) {
        alertService.delete(alertId);
    }

}
