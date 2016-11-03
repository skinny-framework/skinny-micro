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
package eu.medsea.mimeutil.detector;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

public abstract class MimeDetector {

    public final String getName() {
        return getClass().getName();
    }

    public final Collection getMimeTypes(final String fileName) throws UnsupportedOperationException {
        return getMimeTypesFileName(fileName);
    }

    public final Collection getMimeTypes(final File file) throws UnsupportedOperationException {
        return getMimeTypesFile(file);
    }

    public final Collection getMimeTypes(final URL url) throws UnsupportedOperationException {
        return getMimeTypesURL(url);
    }

    public final Collection getMimeTypes(final byte[] data) throws UnsupportedOperationException {
        return getMimeTypesByteArray(data);
    }

    public final Collection getMimeTypes(final InputStream in) throws UnsupportedOperationException {
        // Enforces that the InputStream supports the mark() and reset() methods
        if (!in.markSupported()) {
            throw new UnsupportedOperationException("The InputStream must support the mark() and reset() methods.");
        }
        return getMimeTypesInputStream(in);
    }

    public void init() {
    }

    public void delete() {
    }

    public abstract String getDescription();

    protected abstract Collection getMimeTypesFileName(final String fileName) throws UnsupportedOperationException;

    protected abstract Collection getMimeTypesFile(final File file) throws UnsupportedOperationException;

    protected abstract Collection getMimeTypesURL(final URL url) throws UnsupportedOperationException;

    protected abstract Collection getMimeTypesInputStream(final InputStream in) throws UnsupportedOperationException;

    protected abstract Collection getMimeTypesByteArray(final byte[] data) throws UnsupportedOperationException;

}
