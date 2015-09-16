package flextrade.flexvision.fx.base.service.impl;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import flextrade.flexvision.fx.base.service.FreezableTimeService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FreezableTimeServiceImpl implements FreezableTimeService {
    private ZonedDateTime now;
    private Lock lock = new ReentrantLock();

    @Override
    public void freezeTimeTo(ZonedDateTime timeFrozeTo) {
        try {
            lock.lock();
            log.debug("Freezing time to {}", timeFrozeTo);
            now = timeFrozeTo;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void unfreezeTime() {
        try {
            lock.lock();
            log.debug("Unfreezing time");
            now = null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ZonedDateTime now() {
        return now == null ? ZonedDateTime.now() : now;
    }

    @Override
    public LocalDate valueDate() {
        return now().withZoneSameInstant(ZoneId.of("America/New_York")).toLocalDate();
    }
}
