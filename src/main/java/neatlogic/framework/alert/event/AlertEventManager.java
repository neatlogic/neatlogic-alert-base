/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.alert.event;

import neatlogic.framework.alert.config.AlertConfig;
import neatlogic.framework.alert.dao.mapper.AlertEventMapper;
import neatlogic.framework.alert.dto.AlertEventHandlerVo;
import neatlogic.framework.alert.dto.AlertVo;
import neatlogic.framework.asynchronization.taskmanager.AsyncTaskManager;
import neatlogic.framework.transaction.core.AfterTransactionJob;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AlertEventManager {
    private static final Logger logger = LoggerFactory.getLogger(AlertEventManager.class.getName());
    private static AlertEventMapper alertEventMapper;
    private static AsyncTaskManager<AlertEventJob> manager;

    @Autowired
    public AlertEventManager(AlertEventMapper _alertEventMapper) {
        alertEventMapper = _alertEventMapper;
        manager = AsyncTaskManager.getInstance("ALERT-EVENT-HANDLER", AlertConfig.ALERT_EVENT_THREAD_COUNT(),
                alertEventJob -> {
                    if (CollectionUtils.isNotEmpty(alertEventJob.getHandlerList())) {
                        alertEventJob.execute();
                    }
                });
    }


    static class AlertEventJob {
        private AlertVo alertVo;
        private final List<List<AlertEventHandlerVo>> handlerList;

        public AlertVo getAlertVo() {
            return alertVo;
        }

        public List<List<AlertEventHandlerVo>> getHandlerList() {
            return handlerList;
        }

        public AlertEventJob(List<List<AlertEventHandlerVo>> _handlerList, AlertVo _alertVo) {
            //super("ALERT-EVENT-HANDLER-" + _alertVo.getId());
            handlerList = _handlerList;
            alertVo = _alertVo;
        }

        public void execute() {
            //更换线程名称，方便跟踪
            Thread.currentThread().setName("ALERT-EVENT-HANDLER-" + alertVo.getId());
            for (List<AlertEventHandlerVo> eventHandlerList : handlerList) {
                /*if (eventHandlerList.size() > 1) {
                    BatchRunner<AlertEventHandlerVo> batchRunner = new BatchRunner<>();
                    BatchRunner.State state = new BatchRunner.State();
                    batchRunner.execute(state, eventHandlerList, eventHandlerList.size(), (threadIndex, dataIndex, data) -> {
                        IAlertEventHandler handler = AlertEventHandlerFactory.getHandler(data.getEvent());
                        if (handler != null) {
                            handler.trigger(currentAlertVo);
                        }
                    }, "ALERT-EVENT-HANDLER-BATCH-RUNNER");
                } else*/
                if (eventHandlerList.size() == 1) {
                    AlertEventHandlerVo h = eventHandlerList.get(0);
                    IAlertEventHandler handler = AlertEventHandlerFactory.getHandler(h.getHandler());
                    if (handler != null) {
                        //不断修改alertVo的值，传递给下一个处理器
                        alertVo = handler.trigger(h, alertVo);
                    } else {
                        logger.error("告警事件组件{}不存在", h.getHandler());
                    }
                }
            }
        }
    }


    public static void doEvent(AlertEventType alertEventType, AlertVo alertVo) {
        AfterTransactionJob<AlertEventType> job = new AfterTransactionJob<>("ALERT-EVENT-HANDLER-OFFER");
        job.execute(alertEventType, _alertEventType -> {
            List<AlertEventHandlerVo> handlerList = alertEventMapper.getAlertEventHandlerByEvent(_alertEventType.getName(), alertVo.getType());
            //只需要激活的插件
            handlerList = handlerList.stream().filter(d -> Objects.equals(1, d.getIsActive())).collect(Collectors.toList());
            List<List<AlertEventHandlerVo>> eventHandlerList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(handlerList)) {
                int currentSort = -1;
                List<AlertEventHandlerVo> handlers = new ArrayList<>();
                for (AlertEventHandlerVo handler : handlerList) {
                    if (currentSort != handler.getSort()) {
                        if (CollectionUtils.isNotEmpty(handlers)) {
                            eventHandlerList.add(handlers);
                        }
                        handlers = new ArrayList<>();
                    }
                    handlers.add(handler);
                    currentSort = handler.getSort();
                }

                if (CollectionUtils.isNotEmpty(handlers)) {
                    eventHandlerList.add(handlers);
                }

                if (CollectionUtils.isNotEmpty(eventHandlerList)) {
                    manager.submitTask(new AlertEventJob(eventHandlerList, alertVo));
                }
            }
        });

    }
}
