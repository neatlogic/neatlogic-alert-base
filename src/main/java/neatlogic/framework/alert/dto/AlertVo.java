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
import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.Md5Util;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class AlertVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "唯一值", type = ApiParamType.STRING)
    private String uniqueKey;
    @EntityField(name = "告警次数", type = ApiParamType.INTEGER)
    private int alertCount = 1;
    @EntityField(name = "级别", type = ApiParamType.INTEGER)
    private Integer level;
    @EntityField(name = "标题", type = ApiParamType.STRING)
    private String title;
    @EntityField(name = "类型", type = ApiParamType.LONG)
    private Long type;
    @EntityField(name = "类型唯一标识", type = ApiParamType.STRING)
    private String typeName;
    @EntityField(name = "来源", type = ApiParamType.STRING)
    private String source;
    @EntityField(name = "状态", type = ApiParamType.STRING)
    private String status;
    @EntityField(name = "告警日期", type = ApiParamType.LONG)
    private Date alertTime;
    @EntityField(name = "是否删除", type = ApiParamType.INTEGER)
    private Integer isDelete;
    @EntityField(name = "扩展属性", type = ApiParamType.JSONOBJECT)
    private JSONObject attrObj;
    @JSONField(serialize = false)
    private String attrObjStr;
    @EntityField(name = "对象类型", type = ApiParamType.STRING)
    private String entityType;
    @EntityField(name = "对象名称", type = ApiParamType.STRING)
    private String entityName;
    @EntityField(name = "IP", type = ApiParamType.STRING)
    private String ip;
    @EntityField(name = "端口", type = ApiParamType.STRING)
    private String port;

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }


    public int getAlertCount() {
        return alertCount;
    }

    public void setAlertCount(int alertCount) {
        this.alertCount = alertCount;
    }


    public Integer getLevel() {
        return level;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void generateUniqueKey() {
        String c = "";
        if (StringUtils.isNotBlank(this.title)) {
            c += this.title + "_";
        }
        if (StringUtils.isNotBlank(this.ip)) {
            c += this.ip + "_";
        }
        if (StringUtils.isNotBlank(this.port)) {
            c += this.port + "_";
        }
        if (StringUtils.isNotBlank(this.entityType)) {
            c += this.entityType + "_";
        }
        if (StringUtils.isNotBlank(this.entityName)) {
            c += this.entityName + "_";
        }
        c += this.level;
        this.uniqueKey = Md5Util.encryptMD5(c);
    }

    public Date getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(Date alertTime) {
        this.alertTime = alertTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public JSONObject getAttrObj() {
        if (attrObj == null && StringUtils.isNotBlank(attrObjStr)) {
            try {
                attrObj = JSON.parseObject(attrObjStr);
            } catch (Exception ex) {

            }
        }
        return attrObj;
    }

    public void setAttrObj(JSONObject attrObj) {
        this.attrObj = attrObj;
    }

    public String getAttrObjStr() {
        if (attrObj != null) {
            attrObjStr = attrObj.toJSONString();
        }
        return attrObjStr;
    }

    public void setAttrObjStr(String attrObjStr) {
        this.attrObjStr = attrObjStr;
    }
}
