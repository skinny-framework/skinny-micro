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

import eu.medsea.mimeutil.detector.MimeDetector;
import eu.medsea.util.StringUtil;
import eu.medsea.util.ZipJarUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipException;

public class MimeUtil2 {

    private static Logger log = LoggerFactory.getLogger(MimeUtil2.class);

    public static final MimeType DIRECTORY_MIME_TYPE = new MimeType("application/directory");

    private static Map mimeTypes = Collections.synchronizedMap(new HashMap());

    private MimeDetectorRegistry mimeDetectorRegistry = new MimeDetectorRegistry();

    public static void addKnownMimeType(final MimeType mimeType) {
        addKnownMimeType(mimeType.toString());
    }

    public static void addKnownMimeType(final String mimeType) {
        try {

            String key = getMediaType(mimeType);
            Set s = (Set) mimeTypes.get(key);
            if (s == null) {
                s = new TreeSet();
            }
            s.add(getSubType(mimeType));
            mimeTypes.put(key, s);
        } catch (MimeException ignore) {
            // A couple of entries in the magic mime file don't follow the rules
            // so ignore them
        }
    }

    public MimeDetector registerMimeDetector(final String mimeDetector) {
        return mimeDetectorRegistry.registerMimeDetector(mimeDetector);
    }

    public static String getMediaType(final String mimeType)
            throws MimeException {
        return new MimeType(mimeType).getMediaType();
    }

    public final Collection getMimeTypes(final byte[] data, final MimeType unknownMimeType) throws MimeException {
        Collection mimeTypes = new MimeTypeHashSet();
        if (data == null) {
            log.error("byte array cannot be null.");
        } else {
            if (log.isDebugEnabled()) {
                try {
                    log.debug("Getting MIME types for byte array [" + StringUtil.getHexString(data) + "].");
                } catch (UnsupportedEncodingException e) {
                    throw new MimeException(e);
                }
            }
            mimeTypes.addAll(mimeDetectorRegistry.getMimeTypes(data));

            // We don't want the unknownMimeType added to the collection by MimeDetector(s)
            mimeTypes.remove(unknownMimeType);
        }

        // If the collection is empty we want to add the unknownMimetype
        if (mimeTypes.isEmpty()) {
            mimeTypes.add(unknownMimeType);
        }
        if (log.isDebugEnabled()) {
            log.debug("Retrieved MIME types [" + mimeTypes.toString() + "]");
        }
        return mimeTypes;
    }

    public final Collection getMimeTypes(final File file, final MimeType unknownMimeType) throws MimeException {
        Collection mimeTypes = new MimeTypeHashSet();

        if (file == null) {
            log.error("File reference cannot be null.");
        } else {

            if (log.isDebugEnabled()) {
                log.debug("Getting MIME types for file [" + file.getAbsolutePath() + "].");
            }

            if (file.isDirectory()) {
                mimeTypes.add(MimeUtil2.DIRECTORY_MIME_TYPE);
            } else {
                // Defer this call to the file name and stream methods
                mimeTypes.addAll(mimeDetectorRegistry.getMimeTypes(file));

                // We don't want the unknownMimeType added to the collection by MimeDetector(s)
                mimeTypes.remove(unknownMimeType);
            }
        }
        // If the collection is empty we want to add the unknownMimetype
        if (mimeTypes.isEmpty()) {
            mimeTypes.add(unknownMimeType);
        }
        if (log.isDebugEnabled()) {
            log.debug("Retrieved MIME types [" + mimeTypes.toString() + "]");
        }
        return mimeTypes;
    }

    public final Collection getMimeTypes(final InputStream in, final MimeType unknownMimeType) throws MimeException {
        Collection mimeTypes = new MimeTypeHashSet();

        if (in == null) {
            log.error("InputStream reference cannot be null.");
        } else {
            if (!in.markSupported()) {
                throw new MimeException("InputStream must support the mark() and reset() methods.");
            }
            if (log.isDebugEnabled()) {
                log.debug("Getting MIME types for InputSteam [" + in + "].");
            }
            mimeTypes.addAll(mimeDetectorRegistry.getMimeTypes(in));

            // We don't want the unknownMimeType added to the collection by MimeDetector(s)
            mimeTypes.remove(unknownMimeType);
        }
        // If the collection is empty we want to add the unknownMimetype
        if (mimeTypes.isEmpty()) {
            mimeTypes.add(unknownMimeType);
        }
        if (log.isDebugEnabled()) {
            log.debug("Retrieved MIME types [" + mimeTypes.toString() + "]");
        }
        return mimeTypes;
    }

    public final Collection getMimeTypes(final String fileName, final MimeType unknownMimeType) throws MimeException {
        Collection mimeTypes = new MimeTypeHashSet();

        if (fileName == null) {
            log.error("fileName cannot be null.");
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Getting MIME types for file name [" + fileName + "].");
            }

            // Test if this is a directory
            File file = new File(fileName);

            if (file.isDirectory()) {
                mimeTypes.add(MimeUtil2.DIRECTORY_MIME_TYPE);
            } else {
                mimeTypes.addAll(mimeDetectorRegistry.getMimeTypes(fileName));

                // We don't want the unknownMimeType added to the collection by MimeDetector(s)
                mimeTypes.remove(unknownMimeType);
            }
        }
        // If the collection is empty we want to add the unknownMimetype
        if (mimeTypes.isEmpty()) {
            mimeTypes.add(unknownMimeType);
        }
        if (log.isDebugEnabled()) {
            log.debug("Retrieved MIME types [" + mimeTypes.toString() + "]");
        }
        return mimeTypes;

    }

    public final Collection getMimeTypes(final URL url, final MimeType unknownMimeType) throws MimeException {
        Collection mimeTypes = new MimeTypeHashSet();

        if (url == null) {
            log.error("URL reference cannot be null.");
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Getting MIME types for URL [" + url + "].");
            }

            // Test if this is a directory
            File file = new File(url.getPath());
            if (file.isDirectory()) {
                mimeTypes.add(MimeUtil2.DIRECTORY_MIME_TYPE);
            } else {
                // defer these calls to the file name and stream methods
                mimeTypes.addAll(mimeDetectorRegistry.getMimeTypes(url));

                // We don't want the unknownMimeType added to the collection by MimeDetector(s)
                mimeTypes.remove(unknownMimeType);
            }
        }
        // If the collection is empty we want to add the unknownMimetype
        if (mimeTypes.isEmpty()) {
            mimeTypes.add(unknownMimeType);
        }
        if (log.isDebugEnabled()) {
            log.debug("Retrieved MIME types [" + mimeTypes.toString() + "]");
        }
        return mimeTypes;
    }

    /**
     * Get the most specific match of the Collection of mime types passed in.
     * The Collection
     *
     * @param mimeTypes this should be the Collection of mime types returned
     *                  from a getMimeTypes(...) call.
     * @return the most specific MimeType. If more than one of the mime types in the Collection
     * have the same value then the first one found with this value in the Collection is returned.
     */
    public static MimeType getMostSpecificMimeType(final Collection mimeTypes) {
        MimeType mimeType = null;
        int specificity = 0;
        for (Iterator it = mimeTypes.iterator(); it.hasNext(); ) {
            MimeType mt = (MimeType) it.next();
            if (mt.getSpecificity() > specificity) {
                mimeType = mt;
                specificity = mimeType.getSpecificity();
            }
        }
        return mimeType;
    }

    public static String getSubType(final String mimeType)
            throws MimeException {
        return new MimeType(mimeType).getSubType();
    }

    public static boolean isTextMimeType(final MimeType mimeType) {
        return mimeType instanceof TextMimeType;
    }

    public static InputStream getInputStreamForURL(URL url) throws Exception {
        try {
            return url.openStream();
        } catch (ZipException e) {
            return ZipJarUtil.getInputStreamForURL(url);
        }
    }

}
