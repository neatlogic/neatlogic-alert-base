/*
 * Copyright (C) 2025  深圳极向量科技有限公司 All Rights Reserved.
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

package neatlogic.framework.alert.crossover;

import neatlogic.framework.alert.dto.AlertVo;
import neatlogic.framework.crossover.ICrossoverService;

/*
对告警信息进行向量化处理，用于通过余弦距离检索类似告警
 */
public interface IAlertEmbeddingCrossoverService extends ICrossoverService {
    void saveEmbedding(AlertVo alertVo);
}
