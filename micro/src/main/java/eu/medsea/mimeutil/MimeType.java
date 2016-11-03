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

import java.io.Serializable;
import java.util.regex.Pattern;

public class MimeType implements Comparable, Serializable {

    private static final long serialVersionUID = -1324243127744494894L;

    private static final Pattern mimeSplitter = Pattern.compile("[/;]++");

    protected String mediaType = "*";
    protected String subType = "*";

    //This is a estimate of how specific this mime type is
    private int specificity = 1;

    public MimeType(final String mimeType) throws MimeException {
        if (mimeType == null || mimeType.trim().length() == 0) {
            throw new MimeException("Invalid MimeType [" + mimeType + "]");
        }
        String[] parts = mimeSplitter.split(mimeType.trim());

        if (parts.length > 0) {
            // Treat as the mediaType
            mediaType = getValidMediaType(parts[0]);
        }
        if (parts.length > 1) {
            subType = getValidSubType(parts[1]);
        }
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getSubType() {
        return subType;
    }

    private boolean match(final String mimeType) {
        return toString().equals(mimeType);
    }

    public int hashCode() {
        return (31 * mediaType.hashCode()) + subType.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof MimeType) {
            if (this.mediaType.equals(((MimeType) o).mediaType) && this.subType.equals(((MimeType) o).subType)) {
                return true;
            }
        } else if (o instanceof String) {
            return match((String) o);
        }
        return false;
    }

    public String toString() {
        return mediaType + "/" + subType;
    }

    public int getSpecificity() {
        return specificity;
    }

    void setSpecificity(final int specificity) {
        this.specificity = specificity;
    }

    private String getValidMediaType(final String mediaType) {
        if (mediaType == null || mediaType.trim().length() == 0) {
            return "*";
        }
        return mediaType;
    }

    private String getValidSubType(final String subType) {
        if (subType == null || subType.trim().length() == 0 || "*".equals(mediaType)) {
            // If the mediaType is a wild card then the sub type must also be a wild card
            return "*";
        }
        return subType;
    }

    public int compareTo(Object arg0) {
        if (arg0 instanceof MimeType) {
            return toString().compareTo(((MimeType) arg0).toString());
        }
        return 0;
    }

}
