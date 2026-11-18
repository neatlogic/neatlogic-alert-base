package neatlogic.framework.alert.crossover;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.exception.type.PermissionDeniedException;
import neatlogic.framework.alert.dto.AlertVo;
import neatlogic.framework.crossover.ICrossoverService;
import java.util.List;

/** 告警属性目录与当前事件快照，供消费模块按稳定标识取值。 */
public interface IAlertAttributeSourceService extends ICrossoverService {
    /** 类型关联仅决定推荐顺序，所有启用定义均可使用。 */
    List<JSONObject> definitions(Long alertTypeId);
    /** 保留原始值及其类型，属性缺值允许返回空。 */
    JSONObject values(AlertVo alert);
    /** 按告警详情入口的权限读取预览样例。 */
    AlertVo sample(Long alertId) throws PermissionDeniedException;
}
