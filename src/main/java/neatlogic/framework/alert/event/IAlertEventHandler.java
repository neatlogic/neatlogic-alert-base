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

import neatlogic.framework.alert.dto.AlertEventHandlerConfigVo;
import neatlogic.framework.alert.dto.AlertEventHandlerVo;
import neatlogic.framework.alert.dto.AlertVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface IAlertEventHandler {
    String getName();

    String getLabel();

    String getIcon();

    //定义哪些事件可以使用此插件
    Set<String> supportEventTypes();

    //定义哪些父插件可以添加此插件
    Set<String> supportParentHandler();


    //某些组件可能有子组件，这时需要分拆出自组件的配置，方便调用，不是所有组件都需要返回
    default List<AlertEventHandlerConfigVo> getHandlerConfig(AlertEventHandlerVo alertEventHandlerVo) {
        return new ArrayList<>();
    }

    //根据配置组装子组件，如果一个组件有组件，需要覆盖此方法返回子组件信息
    default void makeupChildHandler(AlertEventHandlerVo alertEventHandlerVo) {

    }

    AlertVo trigger(AlertEventHandlerVo alertEventHandlerVo, AlertVo alertVo);

    AlertVo trigger(AlertEventHandlerVo alertEventHandlerVo, AlertVo alertVo, Long parentAuditId);
}
