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
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.Md5Util;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AlertVo extends BasePageVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @JSONField(serialize = false)
    private List<Long> idList;
    @JSONField(serialize = false)//父告警
    private AlertVo parentAlertVo;
    @EntityField(name = "来源告警id", type = ApiParamType.LONG)
    private Long fromAlertId;
    @EntityField(name = "唯一值", type = ApiParamType.STRING)
    private String uniqueKey;
    @EntityField(name = "级别", type = ApiParamType.INTEGER)
    private Integer level;
    @EntityField(name = "级别名称", type = ApiParamType.STRING)
    private String levelLabel;
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
    @EntityField(name = "状态名称", type = ApiParamType.STRING)
    private String statusName;
    @EntityField(name = "状态颜色", type = ApiParamType.STRING)
    private String statusColor;
    @EntityField(name = "状态状态", type = ApiParamType.STRING)
    private String statusStatus;
    @EntityField(name = "更新时间", type = ApiParamType.LONG)
    private Date updateTime;
    @EntityField(name = "更新时间文本", type = ApiParamType.STRING)
    private String updateTimeStr;
    @EntityField(name = "创建时间", type = ApiParamType.LONG)
    private Date alertTime;
    @EntityField(name = "创建时间文本", type = ApiParamType.STRING)
    private String alertTimeStr;
    @EntityField(name = "是否关闭", type = ApiParamType.INTEGER)
    private int isClose = 0;
    @EntityField(name = "是否关闭文案", type = ApiParamType.STRING)
    private String isCloseName;
    @EntityField(name = "是否删除中", type = ApiParamType.INTEGER)
    private int isDelete = 0;
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
    @EntityField(name = "处理人", type = ApiParamType.JSONARRAY)
    private List<AlertUserVo> userList;
    @EntityField(name = "处理组", type = ApiParamType.JSONARRAY)
    private List<AlertTeamVo> teamList;
    @JSONField(serialize = false)
    private String viewName;//视图唯一标识
    @JSONField(serialize = false)
    private JSONObject rule;//高级搜索条件
    @JSONField(serialize = false)
    private String mode = "simple";//搜索模式
    @EntityField(name = "告警级别", type = ApiParamType.JSONOBJECT)
    private AlertLevelVo alertLevel;
    @EntityField(name = "告警类型", type = ApiParamType.JSONOBJECT)
    private AlertTypeVo alertType;
    @EntityField(name = "告警关系信息", type = ApiParamType.JSONARRAY)
    private List<AlertRelVo> alertRelList;
    @EntityField(name = "子告警数量", type = ApiParamType.INTEGER)
    private int childAlertCount;
    @EntityField(name = "评论", type = ApiParamType.STRING)
    private String comment;
    @JSONField(serialize = false)
    private Integer isChangeChildAlertStatus;
    @JSONField(serialize = false)
    private Integer isCloseChildAlert;
    @EntityField(name = "评论列表", type = ApiParamType.JSONARRAY)
    private List<AlertCommentVo> commentList;
    @JSONField(serialize = false)
    private String applyUserType;
    @JSONField(serialize = false)
    private String applyTeamType;
    @JSONField(serialize = false)
    private List<String> applyUserList;
    @JSONField(serialize = false)
    private List<String> applyTeamList;
    @JSONField(serialize = false)
    private AlertVo fromAlertVo;
    @JSONField(serialize = false)
    private int updateTimeHour;//搜索条件
    private List<AlertAttrFilterVo> attrFilterList;
    @EntityField(name = "处理人uuid列表", type = ApiParamType.JSONARRAY)
    private List<String> userIdList;
    @EntityField(name = "处理组uuid列表", type = ApiParamType.JSONARRAY)
    private List<String> teamIdList;
    @JSONField(serialize = false)//删除批次，用于避免重复触发后台删除
    private Long deleteBatch;
    @JSONField(serialize = false)//搜索模式，决定是否按照fromAlertId来做过滤
    private String searchMode;

    public void addTeam(AlertTeamVo team) {
        if (teamList == null) {
            teamList = new ArrayList<>();
        }
        if (!teamList.contains(team)) {
            teamList.add(team);
        }
    }

    public Long getDeleteBatch() {
        return deleteBatch;
    }

    public String getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(String searchMode) {
        this.searchMode = searchMode;
    }

    public void setDeleteBatch(Long deleteBatch) {
        this.deleteBatch = deleteBatch;
    }

    public List<AlertAttrFilterVo> getAttrFilterList() {
        return attrFilterList;
    }

    public void setAttrFilterList(List<AlertAttrFilterVo> attrFilterList) {
        this.attrFilterList = attrFilterList;
    }

    public void addUser(AlertUserVo userVo) {
        if (userList == null) {
            userList = new ArrayList<>();
        }
        if (!userList.contains(userVo)) {
            userList.add(userVo);
        }
    }

    public int getUpdateTimeHour() {
        return updateTimeHour;
    }

    public String getLevelLabel() {
        if (levelLabel == null && alertLevel != null) {
            levelLabel = alertLevel.getLabel();
        }
        return levelLabel;
    }


    public void setUpdateTimeHour(int updateTimeHour) {
        this.updateTimeHour = updateTimeHour;
    }

    public AlertVo getParentAlertVo() {
        return parentAlertVo;
    }

    public void setParentAlertVo(AlertVo parentAlertVo) {
        this.parentAlertVo = parentAlertVo;
    }

    public AlertVo getFromAlertVo() {
        return fromAlertVo;
    }

    public void setFromAlertVo(AlertVo fromAlertVo) {
        this.fromAlertVo = fromAlertVo;
    }

    public String getIsCloseName() {
        if (this.isClose == 0) {
            return "否";
        } else {
            return "是";
        }
    }


    public String getApplyUserType() {
        return applyUserType;
    }

    public void setApplyUserType(String applyUserType) {
        this.applyUserType = applyUserType;
    }

    public String getApplyTeamType() {
        return applyTeamType;
    }

    public void setApplyTeamType(String applyTeamType) {
        this.applyTeamType = applyTeamType;
    }

    public List<String> getApplyUserList() {
        return applyUserList;
    }

    public void setApplyUserList(List<String> applyUserList) {
        this.applyUserList = applyUserList;
    }

    public List<String> getApplyTeamList() {
        return applyTeamList;
    }

    public void setApplyTeamList(List<String> applyTeamList) {
        this.applyTeamList = applyTeamList;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsChangeChildAlertStatus() {
        return isChangeChildAlertStatus;
    }

    public void setIsChangeChildAlertStatus(Integer isChangeChildAlertStatus) {
        this.isChangeChildAlertStatus = isChangeChildAlertStatus;
    }

    public Integer getIsCloseChildAlert() {
        return isCloseChildAlert;
    }

    public void setIsCloseChildAlert(Integer isCloseChildAlert) {
        this.isCloseChildAlert = isCloseChildAlert;
    }

    public List<AlertCommentVo> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<AlertCommentVo> commentList) {
        this.commentList = commentList;
    }

    public List<AlertUserVo> getUserList() {
        return userList;
    }

    public List<String> getTeamIdList() {
        if (CollectionUtils.isNotEmpty(teamList)) {
            teamIdList = teamList.stream().map(AlertTeamVo::getTeamUuid).collect(Collectors.toList());
        }
        return teamIdList;
    }

    public List<String> getUserIdList() {
        if (CollectionUtils.isNotEmpty(userList)) {
            userIdList = userList.stream().map(AlertUserVo::getUserId).collect(Collectors.toList());
        }
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public void setTeamIdList(List<String> teamIdList) {
        this.teamIdList = teamIdList;
    }

    public void setUserList(List<AlertUserVo> userList) {
        this.userList = userList;
    }

    public List<AlertTeamVo> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<AlertTeamVo> teamList) {
        this.teamList = teamList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getFromAlertId() {
        return fromAlertId;
    }

    public void setFromAlertId(Long fromAlertId) {
        this.fromAlertId = fromAlertId;
    }

    public int getChildAlertCount() {
        return childAlertCount;
    }

    public void setChildAlertCount(int childAlertCount) {
        this.childAlertCount = childAlertCount;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public JSONObject getRule() {
        return rule;
    }

    public void setRule(JSONObject rule) {
        this.rule = rule;
    }

    public String getMode() {
        return mode;
    }

    public AlertTypeVo getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertTypeVo alertType) {
        this.alertType = alertType;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setMode(String mode) {
        this.mode = mode;
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

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public AlertLevelVo getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(AlertLevelVo alertLevel) {
        this.alertLevel = alertLevel;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
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

    public String getAlertTimeStr() {
        if (alertTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            alertTimeStr = sdf.format(alertTime);
        }
        return alertTimeStr;
    }

    public String getUpdateTimeStr() {
        if (updateTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            updateTimeStr = sdf.format(updateTime);
        }
        return updateTimeStr;
    }


    public List<AlertRelVo> getAlertRelList() {
        return alertRelList;
    }

    public void setAlertRelList(List<AlertRelVo> alertRelList) {
        this.alertRelList = alertRelList;
    }

    public int getIsClose() {
        return isClose;
    }

    public void setIsClose(int isClose) {
        this.isClose = isClose;
    }

    public JSONObject getAttrObj() {
        if (attrObj == null && StringUtils.isNotBlank(attrObjStr)) {
            try {
                attrObj = JSON.parseObject(attrObjStr);
            } catch (Exception ignored) {

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

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }


}
