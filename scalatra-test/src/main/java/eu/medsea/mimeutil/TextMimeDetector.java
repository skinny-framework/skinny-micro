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
import eu.medsea.mimeutil.handler.TextMimeHandler;
import eu.medsea.util.EncodingGuesser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class TextMimeDetector extends MimeDetector {

    private static Logger log = LoggerFactory.getLogger(TextMimeDetector.class);

    private static final int BUFFER_SIZE = 1024;

    private static final int MAX_NULL_VALUES = 1;

    private static Collection preferredEncodings = new ArrayList();

    static {
        TextMimeDetector.setPreferredEncodings(new String[]{"UTF-16", "UTF-8", "ISO-8859-1", "windows-1252", "US-ASCII"});
    }

    private static Collection handlers = new ArrayList();

    private TextMimeDetector() {
    }

    TextMimeDetector(int dummy) {
        this();
    }

    public String getDescription() {
        return "Determine if a file or stream contains a text mime type. If so then return TextMimeType with text/plain and the best guess encoding.";
    }

    public Collection getMimeTypesFileName(String fileName) throws UnsupportedOperationException {
        return getMimeTypesFile(new File(fileName));
    }

    public Collection getMimeTypesURL(URL url) throws UnsupportedOperationException {
        InputStream in = null;
        try {
            return getMimeTypesInputStream(in = new BufferedInputStream(MimeUtil.getInputStreamForURL(url)));
        } catch (UnsupportedOperationException e) {
            throw e;
        } catch (Exception e) {
            throw new MimeException(e);
        } finally {
            try {
                in.close();
            } catch (Exception ignore) {
                log.error(ignore.getLocalizedMessage());
            }
        }
    }

    public Collection getMimeTypesFile(File file) throws UnsupportedOperationException {
        if (!file.exists()) {
            throw new UnsupportedOperationException("This MimeDetector requires actual content.");
        }
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            return getMimeTypesInputStream(in);
        } catch (UnsupportedOperationException e) {
            throw e;
        } catch (Exception e) {
            throw new MimeException(e);
        } finally {
            try {
                in.close();
            } catch (Exception ignore) {
                log.error(ignore.getLocalizedMessage());
            }
        }
    }

    public Collection getMimeTypesInputStream(InputStream in) throws UnsupportedOperationException {
        int offset = 0;
        int len = TextMimeDetector.BUFFER_SIZE;
        byte[] data = new byte[len];
        byte[] copy = null;
        // Mark the input stream
        in.mark(len);

        try {
            // Since an InputStream might return only some data (not all
            // requested), we have to read in a loop until
            // either EOF is reached or the desired number of bytes have been
            // read.
            int restBytesToRead = len;
            while (restBytesToRead > 0) {
                int bytesRead = in.read(data, offset, restBytesToRead);
                if (bytesRead < 0)
                    break; // EOF

                offset += bytesRead;
                restBytesToRead -= bytesRead;
            }
            if (offset < len) {
                copy = new byte[offset];
                System.arraycopy(data, 0, copy, 0, offset);
            } else {
                copy = data;
            }
        } catch (IOException ioe) {
            throw new MimeException(ioe);
        } finally {
            try {
                // Reset the input stream to where it was marked.
                in.reset();
            } catch (Exception e) {
                throw new MimeException(e);
            }
        }
        return getMimeTypesByteArray(copy);
    }

    public Collection getMimeTypesByteArray(byte[] data) throws UnsupportedOperationException {

        // Check if the array contains binary data
        if (EncodingGuesser.getSupportedEncodings().isEmpty() || isBinary(data)) {
            throw new UnsupportedOperationException();
        }

        Collection mimeTypes = new ArrayList();

        Collection possibleEncodings = EncodingGuesser.getPossibleEncodings(data);
        if (log.isDebugEnabled()) {
            log.debug("Possible encodings [" + possibleEncodings.size() + "] " + possibleEncodings);
        }

        if (possibleEncodings.isEmpty()) {
            // Is not a text file understood by this JVM
            throw new UnsupportedOperationException();
        }

        String encoding = null;
        // Iterate over the preferedEncodings array in the order defined and return the first one found
        for (Iterator it = TextMimeDetector.preferredEncodings.iterator(); it.hasNext(); ) {
            encoding = (String) it.next();
            if (possibleEncodings.contains(encoding)) {
                mimeTypes.add(new TextMimeType("text/plain", encoding));
                break;
            }
        }
        // If none of the preferred encodings were acceptable lets see if the default encoding can be used.
        if (mimeTypes.isEmpty() && possibleEncodings.contains(EncodingGuesser.getDefaultEncoding())) {
            encoding = EncodingGuesser.getDefaultEncoding();
            mimeTypes.add(new TextMimeType("text/plain", encoding));
        }

        // If none of our preferredEncodings or the default encoding are in the possible encodings list we return the first possibleEncoding;
        if (mimeTypes.isEmpty()) {
            Iterator it = possibleEncodings.iterator();
            encoding = (String) it.next();
            mimeTypes.add(new TextMimeType("text/plain", encoding));
        }

        if (mimeTypes.isEmpty() || handlers.isEmpty()) {
            // Nothing to handle
            return mimeTypes;
        }

        // String will be passed in as  is currently in the encoding defined by encoding
        try {
            int lengthBOM = EncodingGuesser.getLengthBOM(encoding, data);
            String content = new String(EncodingGuesser.getByteArraySubArray(data, lengthBOM, data.length - lengthBOM), encoding);
            return fireMimeHandlers(mimeTypes, content);
        } catch (UnsupportedEncodingException ignore) {
            // This should never, never, never happen
        }
        return mimeTypes;
    }

    public static void setPreferredEncodings(String[] encodings) {
        TextMimeDetector.preferredEncodings = EncodingGuesser.getValidEncodings(encodings);
        if (log.isDebugEnabled()) {
            log.debug("Preferred Encodings set to " + TextMimeDetector.preferredEncodings);
        }
    }

    private Collection fireMimeHandlers(Collection mimeTypes, String content) {
        // We only have one entry in the mimeTypes Collection due to the way
        // this MimeDetector works.
        TextMimeType mimeType = (TextMimeType) mimeTypes.iterator().next();

        for (Iterator it = handlers.iterator(); it.hasNext(); ) {
            TextMimeHandler tmh = (TextMimeHandler) it.next();
            if (tmh.handle(mimeType, content)) {
                // The first handler to return true will short circuit the rest of the handlers
                break;
            }
        }
        return mimeTypes;
    }

    private boolean isBinary(byte[] data) {
        int negCount = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {
                negCount++;
            } else {
                negCount = 0;
            }
            if (negCount == MAX_NULL_VALUES) {
                return true;
            }
        }
        return false;
    }

}
