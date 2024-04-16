package org.dudu.TomcatVuln.Interface.strategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 选择漏洞检测策略
 * @author：DUDU
 * @date: 2023/9/26
 **/
public class StrategyChoose {
    private static  Map<String,ExecuteStrategy> executeCheckStrategyMap= new HashMap();
    private static  Map<String,ExecuteStrategy> executeExploitStrategyMap= new HashMap();

    public static ExecuteStrategy chooseCheck(String mark){
        return executeCheckStrategyMap.get(mark);
    }

    public static ExecuteStrategy chooseExploit(String mark){
        return executeExploitStrategyMap.get(mark);
    }

    public static  void putCheckStrategy(String mark,ExecuteStrategy executeStrategy){
        executeCheckStrategyMap.put(mark,executeStrategy);
    }

    public static void putExploitStrategy(String mark,ExecuteStrategy executeStrategy){
        executeExploitStrategyMap.put(mark,executeStrategy);
    }
}
