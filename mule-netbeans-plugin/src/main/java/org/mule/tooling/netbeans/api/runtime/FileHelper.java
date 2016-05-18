/*
 * Copyright 2016 Facundo Lopez Kaufmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mule.tooling.netbeans.api.runtime;

import java.util.regex.Pattern;

/**
 *
 * @author Facundo Lopez Kaufmann
 */
public class FileHelper {

    private static final Pattern JAR_PATTERN = Pattern.compile("(.*?)\\.jar"); // NOI18N

    public static boolean isJar(String name) {
        return JAR_PATTERN.matcher(name).matches();
    }
}