package com.starnetsecurity.parkClientServer.service;

import com.starnetsecurity.parkClientServer.entity.SyncRule;

import java.util.List;

/**
 * Created by 宏炜 on 2017-10-09.
 */
public interface SyncRuleService {

    List<SyncRule> getSyncRuleList();

    void updateExecuteSyncRule(List<SyncRule> syncRules) throws Exception;

}
