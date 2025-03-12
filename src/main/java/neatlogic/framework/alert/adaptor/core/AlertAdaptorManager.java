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

package neatlogic.framework.alert.adaptor.core;

import com.alibaba.fastjson.JSONObject;
import com.neatlogic.alert.plugin.adapter.core.IAdapter;
import neatlogic.framework.alert.dto.AlertTypeAdaptorVo;
import neatlogic.framework.alert.dto.AlertTypeVo;
import neatlogic.framework.common.util.FileUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class AlertAdaptorManager {
    static Map<String, IAdapter> adapterMap = new ConcurrentHashMap<>();
    static Map<String, Long> fileIdMap = new ConcurrentHashMap<>();

    public static void removeAdapter(String name) {
        removeAdapter(name, null);
    }

    public static void removeAdapter(String name, String adaptor) {
        String key;
        if (StringUtils.isNotBlank(adaptor)) {
            key = name + "#" + adaptor;
            adapterMap.remove(key);
        } else {
            // 用迭代器的 remove 方法安全删除
            adapterMap.entrySet().removeIf(entry -> entry.getKey().startsWith(name + "#"));
        }
    }

    private static File downloadJar(InputStream is) throws IOException {
        File tempFile = File.createTempFile("neatlogic-alert-plugin-", ".jar");
        tempFile.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }

    public static JSONObject convert(AlertTypeVo alertTypeVo, AlertTypeAdaptorVo adaptorVo, String alertContent) throws Exception {
        //如果fileId变了，代表附件已经更换，需要先清理缓存
        if (fileIdMap.containsKey(alertTypeVo.getName() + "#" + adaptorVo.getName())
                && !Objects.equals(fileIdMap.get(alertTypeVo.getName() + "#" + adaptorVo.getName()), adaptorVo.getFileId())) {
            adapterMap.remove(alertTypeVo.getName() + "#" + adaptorVo.getName());
        }

        fileIdMap.put(alertTypeVo.getName() + "#" + adaptorVo.getName(), adaptorVo.getFileId());

        if (!adapterMap.containsKey(alertTypeVo.getName() + "#" + adaptorVo.getName())) {
            AlertAdapterLoader classLoader = new AlertAdapterLoader(downloadJar(FileUtil.getData(adaptorVo.getFilePath())), IAdapter.class.getClassLoader());
            ServiceLoader<IAdapter> loader = ServiceLoader.load(IAdapter.class, classLoader);
            for (IAdapter adapter : loader) {
                adapterMap.put(alertTypeVo.getName() + "#" + adaptorVo.getName(), adapter);
            }
        }
        IAdapter adapter = adapterMap.get(alertTypeVo.getName() + "#" + adaptorVo.getName());
        return adapter.convert(alertContent);
    }
}
