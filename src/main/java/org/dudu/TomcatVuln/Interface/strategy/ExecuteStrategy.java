package org.dudu.TomcatVuln.Interface.strategy;

import java.util.concurrent.ExecutionException;

/**
 * @description: 执行策略
 * @author：DUDU
 * @date: 2023/9/26
 **/
public interface ExecuteStrategy<T> {

    String mark();

    String execute(T data) throws Exception;
}
