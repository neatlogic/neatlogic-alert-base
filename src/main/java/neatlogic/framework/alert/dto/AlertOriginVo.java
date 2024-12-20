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

package neatlogic.framework.alert.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.alert.enums.AlertOriginStatus;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class AlertOriginVo {
    private Long id;
    private String contentStr;
    private JSONObject content;
    private String type;
    private Date time;
    private String error;
    private String status;
    private String statusText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentStr() {
        if (content != null) {
            contentStr = JSON.toJSONString(content);
        }
        return contentStr;
    }

    public void setContentStr(String contentStr) {
        this.contentStr = contentStr;
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

    public JSONObject getContent() {
        if (content == null && StringUtils.isNotBlank(contentStr)) {
            try {
                content = JSON.parseObject(contentStr);
            } catch (Exception ignored) {

            }
        }
        return content;
    }

    public void setContent(JSONObject content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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
