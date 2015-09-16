package flextrade.flexvision.fx.base.service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface TaskRunner {
    void submit(Runnable task);

    <T> Future<T> submit(Callable<T> task);
}
