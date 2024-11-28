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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AlertAdapterLoader extends URLClassLoader {
    public AlertAdapterLoader(File jarFile, ClassLoader parent) throws IOException {
        super(new URL[]{jarFile.toURI().toURL()}, parent);
        addDependencies(jarFile);
    }


    private void addDependencies(File jarFile) throws IOException {
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                //加载本地依赖包
                if (entry.getName().startsWith("META-INF/lib/") && entry.getName().endsWith(".jar")) {
                    File tempFile = File.createTempFile("neatlogic-alert-plugin-embed-", ".jar");
                    tempFile.deleteOnExit();
                    try (InputStream in = jar.getInputStream(entry);
                         OutputStream out = Files.newOutputStream(tempFile.toPath())) {
                        copyStream(in, out);
                    }
                    this.addURL(tempFile.toURI().toURL());
                }
            }
        }
    }

    private void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
    }
}
