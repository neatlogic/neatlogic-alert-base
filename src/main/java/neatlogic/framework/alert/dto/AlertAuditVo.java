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

package neatlogic.framework.alert.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.asynchronization.threadlocal.InputFromContext;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.constvalue.InputFrom;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class AlertAuditVo extends BasePageVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "告警id", type = ApiParamType.LONG)
    private Long alertId;
    @EntityField(name = "属性名称", type = ApiParamType.STRING)
    private String attrName;
    @EntityField(name = "旧值", type = ApiParamType.JSONARRAY)
    private JSONArray oldValueList;
    @EntityField(name = "新值", type = ApiParamType.JSONARRAY)
    private JSONArray newValueList;
    @JSONField(serialize = false)
    private String oldValueListStr;
    @JSONField(serialize = false)
    private String newValueListStr;
    @EntityField(name = "输入用户", type = ApiParamType.STRING)
    private String inputUser;
    @EntityField(name = "输入时间", type = ApiParamType.LONG)
    private Date inputTime;
    @EntityField(name = "更新源头", type = ApiParamType.STRING)
    private String inputFrom;
    @EntityField(name = "更新源头名称", type = ApiParamType.STRING)
    private String inputFromName;

    public AlertAuditVo() {

    }

    public AlertAuditVo(Boolean isInit) {
        if (isInit) {
            this.setInputFrom(InputFromContext.get().getInputFrom());
            this.setInputUser(UserContext.get().getUserUuid(true));
        }
    }

    public void addNewValue(Object value) {
        if (newValueList == null) {
            newValueList = new JSONArray();
        }
        if (!newValueList.contains(value)) {
            newValueList.add(value);
        }
    }

    public void addOldValue(Object value) {
        if (oldValueList == null) {
            oldValueList = new JSONArray();
        }
        if (!oldValueList.contains(value)) {
            oldValueList.add(value);
        }
    }

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public JSONArray getOldValueList() {
        if (oldValueList == null && StringUtils.isNotBlank(oldValueListStr)) {
            try {
                oldValueList = JSON.parseArray(oldValueListStr);
            } catch (Exception ignored) {
            }
        }
        return oldValueList;
    }

    public void setOldValueList(JSONArray oldValueList) {
        this.oldValueList = oldValueList;
    }

    public String getInputFromName() {
        if (StringUtils.isNotBlank(inputFrom)) {
            inputFromName = InputFrom.getText(inputFrom);
        }
        return inputFromName;
    }

    public void setInputFromName(String inputFromName) {
        this.inputFromName = inputFromName;
    }

    public JSONArray getNewValueList() {
        if (newValueList == null && StringUtils.isNotBlank(newValueListStr)) {
            try {
                newValueList = JSON.parseArray(newValueListStr);
            } catch (Exception ignored) {
            }
        }
        return newValueList;
    }

    public void setNewValueList(JSONArray newValueList) {
        this.newValueList = newValueList;
    }

    public String getOldValueListStr() {
        if (oldValueList != null) {
            oldValueListStr = JSON.toJSONString(oldValueList);
        }
        return oldValueListStr;
    }

    public void setOldValueListStr(String oldValueListStr) {
        this.oldValueListStr = oldValueListStr;
    }

    public String getNewValueListStr() {
        if (newValueList != null) {
            newValueListStr = JSON.toJSONString(newValueList);
        }
        return newValueListStr;
    }

    public void setNewValueListStr(String newValueListStr) {
        this.newValueListStr = newValueListStr;
    }

    public String getInputUser() {
        return inputUser;
    }

    public void setInputUser(String inputUser) {
        this.inputUser = inputUser;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public String getInputFrom() {
        return inputFrom;
    }

    public void setInputFrom(String inputFrom) {
        this.inputFrom = inputFrom;
    }
}
