/*
 * Copyright 2007-2009 Medsea Business Solutions S.L.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.medsea.mimeutil;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Set;

public class MimeUtil {

    public static void addKnownMimeType(final MimeType mimeType) {
        MimeUtil2.addKnownMimeType(mimeType);
    }

    public static InputStream getInputStreamForURL(URL url) throws Exception {
        return MimeUtil2.getInputStreamForURL(url);
    }

}
