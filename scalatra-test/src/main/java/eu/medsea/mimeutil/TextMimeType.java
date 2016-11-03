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

import eu.medsea.util.EncodingGuesser;

public class TextMimeType extends MimeType {

    private static final long serialVersionUID = -4798584119063522367L;

    // The default encoding is always set Unknown
    private String encoding = "Unknown";

    public TextMimeType(final String mimeType, final String encoding) {
        super(mimeType);
        this.encoding = getValidEncoding(encoding);
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String toString() {
        return super.toString() + ";charset=" + getEncoding();
    }

    private boolean isKnownEncoding(String encoding) {
        return EncodingGuesser.isKnownEncoding(encoding);
    }

    private String getValidEncoding(String encoding) {
        if (isKnownEncoding(encoding)) {
            return encoding;
        } else {
            return "Unknown";
        }
    }

}
