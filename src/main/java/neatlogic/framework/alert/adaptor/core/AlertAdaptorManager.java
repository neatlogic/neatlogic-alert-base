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

package neatlogic.framework.alert.adaptor.core;

import com.alibaba.fastjson.JSONObject;
import com.neatlogic.alert.plugin.adapter.core.IAdapter;
import neatlogic.framework.alert.dto.AlertTypeAdaptorVo;
import neatlogic.framework.alert.dto.AlertTypeVo;
import neatlogic.framework.alert.exception.alertadaptor.AlertAdaptorNotFoundException;
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
        String key = alertTypeVo.getName().toLowerCase() + "#" + adaptorVo.getName().toLowerCase();
        if (fileIdMap.containsKey(key)
                && !Objects.equals(fileIdMap.get(key), adaptorVo.getFileId())) {
            adapterMap.remove(key);
        }

        fileIdMap.put(key, adaptorVo.getFileId());

        if (!adapterMap.containsKey(key)) {
            AlertAdapterLoader classLoader = new AlertAdapterLoader(downloadJar(FileUtil.getData(adaptorVo.getFilePath())), IAdapter.class.getClassLoader());
            ServiceLoader<IAdapter> loader = ServiceLoader.load(IAdapter.class, classLoader);
            for (IAdapter adapter : loader) {
                adapterMap.put(key, adapter);
            }
        }
        IAdapter adapter = adapterMap.get(key);
        if (adapter == null) {
            throw new AlertAdaptorNotFoundException(key);
        }
        return adapter.convert(alertContent);
    }
}
