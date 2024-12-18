/*
 * Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package neatlogic.framework.alert.event;

import neatlogic.framework.alert.dao.mapper.AlertEventMapper;
import neatlogic.framework.alert.dto.AlertEventHandlerVo;
import neatlogic.framework.alert.dto.AlertVo;
import neatlogic.framework.asynchronization.queue.NeatLogicBlockingQueue;
import neatlogic.framework.asynchronization.thread.NeatLogicThread;
import neatlogic.framework.asynchronization.threadpool.CachedThreadPool;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.transaction.core.AfterTransactionJob;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;

@RootComponent
public class AlertEventManager {
    private static final Logger logger = LoggerFactory.getLogger(AlertEventManager.class.getName());
    private static final NeatLogicBlockingQueue<AlertEventJob> eventHandlerQueue = new NeatLogicBlockingQueue<>(new LinkedBlockingQueue<>());
    private static final ConcurrentSkipListMap<Long, AlertEventJob> inQueueAlertMap = new ConcurrentSkipListMap<>();
    private static AlertEventMapper alertEventMapper;

    @Autowired
    public AlertEventManager(AlertEventMapper _alertEventMapper) {
        alertEventMapper = _alertEventMapper;
    }

    @PostConstruct
    public void init() {
        Thread t = new Thread(new NeatLogicThread("ALERT-EVENT-HANDLER") {
            @Override
            protected void execute() {
                AlertEventJob alertEventJob;
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        alertEventJob = eventHandlerQueue.take();
                        if (alertEventJob != null && CollectionUtils.isNotEmpty(alertEventJob.getHandlerList())) {
                            //把正在处理中的告警信息放入inQueueAlertMap，后续处理器在处理数据时在数据库查询不到可以从这里获取
                            inQueueAlertMap.put(alertEventJob.getAlertVo().getId(), alertEventJob);
                            CachedThreadPool.execute(alertEventJob);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    static class AlertEventJob extends NeatLogicThread {
        //private final AlertEventHandlerVo eventHandlerVo;
        private AlertVo alertVo;
        private final List<List<AlertEventHandlerVo>> handlerList;

        public AlertVo getAlertVo() {
            return alertVo;
        }

        public List<List<AlertEventHandlerVo>> getHandlerList() {
            return handlerList;
        }

        public AlertEventJob(List<List<AlertEventHandlerVo>> _handlerList, AlertVo _alertVo) {
            super("ALERT-EVENT-HANDLER-" + _alertVo.getId());
            handlerList = _handlerList;
            alertVo = _alertVo;
        }

        @Override
        protected void execute() {
            try {
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
            } finally {
                inQueueAlertMap.remove(alertVo.getId());
            }
        }
    }


    public static void doEvent(AlertEventType alertEventType, AlertVo alertVo) {
        AfterTransactionJob<AlertEventType> job = new AfterTransactionJob<>("ALERT-EVENT-HANDLER-OFFER");
        job.execute(alertEventType, _alertEventType -> {
            List<AlertEventHandlerVo> handlerList = alertEventMapper.getAlertEventHandlerByEvent(_alertEventType.getName());
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
                    eventHandlerQueue.offer(new AlertEventJob(eventHandlerList, alertVo));
                }
            }
        });

    }
}
