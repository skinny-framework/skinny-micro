package eu.medsea.mimeutil;

import eu.medsea.mimeutil.detector.MimeDetector;
import eu.medsea.util.EncodingGuesser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

class MimeDetectorRegistry {

    private static Logger log = LoggerFactory.getLogger(MimeDetectorRegistry.class);

    private TextMimeDetector TextMimeDetector = new TextMimeDetector(1);

    private Map mimeDetectors = new TreeMap();

    MimeDetector registerMimeDetector(final String mimeDetector) {
        if (mimeDetectors.containsKey(mimeDetector)) {
            log.warn("MimeDetector [" + mimeDetector + "] will not be registered as a MimeDetector with this name is already registered.");
            return (MimeDetector) mimeDetectors.get(mimeDetector);
        }
        // Create the mime detector if we can
        try {
            MimeDetector md = (MimeDetector) Class.forName(mimeDetector).newInstance();
            md.init();
            if (log.isDebugEnabled()) {
                log.debug("Registering MimeDetector with name [" + md.getName() + "] and description [" + md.getDescription() + "]");
            }
            mimeDetectors.put(mimeDetector, md);
            return md;
        } catch (Exception e) {
            log.error("Exception while registering MimeDetector [" + mimeDetector + "].", e);
        }
        // Failed to create an instance
        return null;
    }

    MimeDetector getMimeDetector(final String name) {
        return (MimeDetector) mimeDetectors.get(name);
    }

    Collection getMimeTypes(final byte[] data) throws MimeException {
        Collection mimeTypes = new ArrayList();
        try {
            if (!EncodingGuesser.getSupportedEncodings().isEmpty()) {
                mimeTypes = TextMimeDetector.getMimeTypes(data);
            }
        } catch (UnsupportedOperationException ignore) {
            // The TextMimeDetector will throw this if it decides
            // the content is not text
        }
        for (Iterator it = mimeDetectors.values().iterator(); it.hasNext(); ) {
            try {
                MimeDetector md = (MimeDetector) it.next();
                mimeTypes.addAll(md.getMimeTypes(data));
            } catch (UnsupportedOperationException ignore) {
                // We ignore this as it indicates that this MimeDetector does not support
                // Getting mime types from files
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return mimeTypes;
    }


    Collection getMimeTypes(final String fileName) throws MimeException {
        Collection mimeTypes = new ArrayList();
        try {
            if (!EncodingGuesser.getSupportedEncodings().isEmpty()) {
                mimeTypes = TextMimeDetector.getMimeTypes(fileName);
            }
        } catch (UnsupportedOperationException ignore) {
            // The TextMimeDetector will throw this if it decides
            // the content is not text
        }
        for (Iterator it = mimeDetectors.values().iterator(); it.hasNext(); ) {
            try {
                MimeDetector md = (MimeDetector) it.next();
                mimeTypes.addAll(md.getMimeTypes(fileName));
            } catch (UnsupportedOperationException usoe) {
                // We ignore this as it indicates that this MimeDetector does not support
                // Getting mime types from streams
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return mimeTypes;
    }

    Collection getMimeTypes(final File file) throws MimeException {
        Collection mimeTypes = new ArrayList();
        try {
            if (!EncodingGuesser.getSupportedEncodings().isEmpty()) {
                mimeTypes = TextMimeDetector.getMimeTypes(file);
            }
        } catch (UnsupportedOperationException ignore) {
            // The TextMimeDetector will throw this if it decides
            // the content is not text
        }
        for (Iterator it = mimeDetectors.values().iterator(); it.hasNext(); ) {
            try {
                MimeDetector md = (MimeDetector) it.next();
                mimeTypes.addAll(md.getMimeTypes(file));
            } catch (UnsupportedOperationException usoe) {
                // We ignore this as it indicates that this MimeDetector does not support
                // Getting mime types from streams
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return mimeTypes;
    }

    Collection getMimeTypes(final InputStream in) throws MimeException {
        Collection mimeTypes = new ArrayList();
        try {
            if (!EncodingGuesser.getSupportedEncodings().isEmpty()) {
                mimeTypes = TextMimeDetector.getMimeTypes(in);
            }
        } catch (UnsupportedOperationException ignore) {
            // The TextMimeDetector will throw this if it decides
            // the content is not text
        }
        for (Iterator it = mimeDetectors.values().iterator(); it.hasNext(); ) {
            try {
                MimeDetector md = (MimeDetector) it.next();
                mimeTypes.addAll(md.getMimeTypes(in));
            } catch (UnsupportedOperationException usoe) {
                // We ignore this as it indicates that this MimeDetector does not support
                // Getting mime types from streams
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return mimeTypes;
    }

    Collection getMimeTypes(final URL url) throws MimeException {
        Collection mimeTypes = new ArrayList();
        try {
            if (!EncodingGuesser.getSupportedEncodings().isEmpty()) {
                mimeTypes = TextMimeDetector.getMimeTypes(url);
            }
        } catch (UnsupportedOperationException ignore) {
            // The TextMimeDetector will throw this if it decides
            // the content is not text
        }
        for (Iterator it = mimeDetectors.values().iterator(); it.hasNext(); ) {
            try {
                MimeDetector md = (MimeDetector) it.next();
                mimeTypes.addAll(md.getMimeTypes(url));
            } catch (UnsupportedOperationException usoe) {
                // We ignore this as it indicates that this MimeDetector does not support
                // Getting mime types from streams
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return mimeTypes;
    }

}
