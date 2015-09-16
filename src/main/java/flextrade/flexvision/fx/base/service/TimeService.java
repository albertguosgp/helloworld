package flextrade.flexvision.fx.base.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public interface TimeService {
    ZonedDateTime now();

    static public String toISO8601Format(Date date) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return zonedDateTime.toInstant().toString();
    }
}
