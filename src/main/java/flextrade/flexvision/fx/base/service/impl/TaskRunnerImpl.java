package flextrade.flexvision.fx.base.service.impl;

import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import flextrade.flexvision.fx.base.service.TaskRunner;

@Service
public class TaskRunnerImpl implements TaskRunner {
    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void submit(Runnable task) {
        executorService.submit(task);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executorService.submit(task);
    }
}
