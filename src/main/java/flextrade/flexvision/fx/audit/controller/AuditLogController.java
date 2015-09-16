package flextrade.flexvision.fx.audit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import flextrade.flexvision.fx.audit.json.AuditLogQuery;
import flextrade.flexvision.fx.audit.pojo.AuditLog;
import flextrade.flexvision.fx.audit.service.AuditLogService;

import static com.google.common.collect.ImmutableMap.of;

@RestController
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @RequestMapping(value = "/auditlog",
            method = RequestMethod.GET)
    public List<AuditLog> getAuditLog(AuditLogQuery auditLogQuery) {
        return auditLogService.get(auditLogQuery);
    }

    @RequestMapping(value = "/auditlog", method = RequestMethod.POST)
    public Map<String, Long> saveAuditLog(@Valid @RequestBody AuditLog auditLog) {
        Long auditLogId = auditLogService.save(auditLog);
        return of("auditLogId", auditLogId);
    }

    @RequestMapping(value = "/auditlog/{auditLogId}", method = RequestMethod.DELETE)
    public void deleteAuditLog(@PathVariable Long auditLogId) {
        auditLogService.delete(auditLogId);
    }
}


