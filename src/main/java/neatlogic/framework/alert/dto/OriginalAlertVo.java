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
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.alert.enums.AlertOriginStatus;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.constvalue.InputFrom;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class OriginalAlertVo extends BasePageVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @JSONField(serialize = false)
    private List<Long> idList;
    @EntityField(name = "类型", type = ApiParamType.STRING)
    private String type;
    @EntityField(name = "转换器名称", type = ApiParamType.STRING)
    private String adaptor;
    @EntityField(name = "来源", type = ApiParamType.STRING)
    private String source;
    @EntityField(name = "来源名称", type = ApiParamType.STRING)
    private String sourceName;
    @EntityField(name = "内容", type = ApiParamType.STRING)
    private String content;
    @EntityField(name = "时间", type = ApiParamType.STRING)
    private Date time;
    @EntityField(name = "异常", type = ApiParamType.STRING)
    private String error;
    @EntityField(name = "状态", type = ApiParamType.STRING)
    private String status;
    @EntityField(name = "状态名称", type = ApiParamType.STRING)
    private String statusText;
    @JSONField(serialize = false)
    private List<String> timeRange;
    @EntityField(name = "高亮数据", type = ApiParamType.JSONOBJECT)
    private Map<String, List<String>> highlightMap;
    @EntityField(name = "告警数据", type = ApiParamType.JSONOBJECT)
    private JSONObject alertData;
    @JSONField(serialize = false)
    private String alertDataStr;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public JSONObject getAlertData() {
        if (alertData == null && StringUtils.isNotBlank(alertDataStr)) {
            try {
                alertData = JSON.parseObject(alertDataStr);
            } catch (Exception ignored) {

            }
        }
        return alertData;
    }

    public void setAlertData(JSONObject alertData) {
        this.alertData = alertData;
    }

    public String getAlertDataStr() {
        if (alertData != null) {
            alertDataStr = alertData.toJSONString();
        }
        return alertDataStr;
    }

    public void setAlertDataStr(String alertDataStr) {
        this.alertDataStr = alertDataStr;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public Map<String, List<String>> getHighlightMap() {
        return highlightMap;
    }

    public void setHighlightMap(Map<String, List<String>> highlightMap) {
        this.highlightMap = highlightMap;
    }

    public String getStatusText() {
        if (StringUtils.isNotBlank(status)) {
            statusText = AlertOriginStatus.getText(status);
        }
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getSourceName() {
        if (StringUtils.isNotBlank(source)) {
            sourceName = InputFrom.getText(source);
        }
        return sourceName;
    }

    public String getAdaptor() {
        return adaptor;
    }

    public void setAdaptor(String adaptor) {
        this.adaptor = adaptor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(List<String> timeRange) {
        this.timeRange = timeRange;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
