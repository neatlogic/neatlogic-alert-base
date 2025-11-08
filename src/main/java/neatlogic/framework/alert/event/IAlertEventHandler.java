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

import neatlogic.framework.alert.dto.AlertEventHandlerVo;
import neatlogic.framework.alert.dto.AlertVo;

import java.util.Set;

public interface IAlertEventHandler {
    //是否异步
    boolean isAsync();

    String getName();

    String getLabel();

    String getIcon();

    String getDescription();

    default int getSort() {
        return 99;
    }

    //定义哪些事件可以使用此插件
    Set<String> supportEventTypes();

    //定义哪些父插件可以添加此插件
    Set<String> supportParentHandler();


    //某些组件可能有子组件，这时需要分拆出自组件的配置，方便调用，不是所有组件都需要返回
    /*default List<AlertEventHandlerConfigVo> getHandlerConfig(AlertEventHandlerVo alertEventHandlerVo) {
        return new ArrayList<>();
    }*/

    //根据配置组装子组件，如果一个组件有组件，需要覆盖此方法返回子组件信息
    default void makeupChildHandler(AlertEventHandlerVo alertEventHandlerVo) {

    }

    AlertVo trigger(AlertEventHandlerVo alertEventHandlerVo, AlertVo alertVo);

    AlertVo trigger(AlertEventHandlerVo alertEventHandlerVo, AlertVo alertVo, Long parentAuditId);
}
