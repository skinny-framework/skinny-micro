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
package eu.medsea.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

public class EncodingGuesser {

    private static Logger log = LoggerFactory.getLogger(EncodingGuesser.class);

    // We want the CANONICAL name of the default Charset for the JVM.
    private static String defaultJVMEncoding = Charset.forName(
            new java.io.OutputStreamWriter(new java.io.ByteArrayOutputStream()).getEncoding()).name();

    private static Collection supportedEncodings = new TreeSet();

    private static Map boms = new HashMap();

    static {
        // We have this switched off by default. If you want to initialise with all encodings
        // supported by your JVM the just un-comment the following line
        // EncodingGuesser.supportedEncodings = getCanonicalEncodingNamesSupportedByJVM();

        // Initialise some known BOM (s) keyed by their canonical encoding name.
        boms.put("UTF-32BE", new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF});
        boms.put("UTF-32LE", new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00});
        boms.put("UTF-16BE", new byte[]{(byte) 0xFE, (byte) 0xFF});
        boms.put("UTF-16LE", new byte[]{(byte) 0xFF, (byte) 0xFE});
        boms.put("UTF-8", new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        boms.put("UTF-7", new byte[]{(byte) 0x2B, (byte) 0x2F, (byte) 0x76}); // We may need to cater for the next char as well which can be one of [38 | 39 | 2B | 2F]
        boms.put("UTF-1", new byte[]{(byte) 0xF7, (byte) 0x64, (byte) 0x4C});
        boms.put("UTF-EBCDIC", new byte[]{(byte) 0xDD, (byte) 0x73, (byte) 0x66, (byte) 0x73});
        boms.put("SCSU", new byte[]{(byte) 0x0E, (byte) 0xFE, (byte) 0xFF});
        boms.put("BOCU-1", new byte[]{(byte) 0xFB, (byte) 0xEE, (byte) 0x28}); // optionally followed by 0xFF

    }

    public static boolean isKnownEncoding(String encoding) {
        return supportedEncodings.contains(encoding);
    }

    public static Collection getPossibleEncodings(byte[] data) {

        Collection possibleEncodings = new TreeSet();
        if (data == null || data.length == 0) {
            return possibleEncodings;
        }

        // We may have to take account of a BOM (Byte Order Mark) as this could be present at the beginning of
        // the source byte array. These sequences may match valid bytes at the beginning of binary data but this shouldn't
        // match any encodings anyway.

        String encoding = null;
        for (Iterator it = supportedEncodings.iterator(); it.hasNext(); ) {
            // This will eliminate encodings it can't possibly be from the supported encodings
            // by converting the source byte array to a String using each encoding in turn and
            // then getting the resultant byte array and checking it against the passed in data.

            try {
                // One problem to overcome is that the passed in data may be terminated by an
                // incomplete character for the current encoding so we need to remove the last character
                // then get the resulting bytes and only match this against the source byte array.

                encoding = (String) it.next();

                // Check if this encoding has a known bom and if so does it match the beginning of the data array ?
                // returns either 0 or the length of the bom
                int lengthBOM = getLengthBOM(encoding, data);

                // Don't use the BOM when constructing the String
                String test = new String(getByteArraySubArray(data, lengthBOM, data.length - lengthBOM), encoding);

                // Only remove the last character if the String is more than 1 character long
                if (test.length() > 1) {
                    // Remove last character from the test string.
                    test = test.substring(0, test.length() - 2);
                }

                // This is the byte array we will compare with the passed in source array copy
                byte[] compare = null;
                try {
                    compare = test.getBytes(encoding);
                } catch (UnsupportedOperationException ignore) {
                    continue;
                }

                // Check if source and destination byte arrays are equal
                if (!compareByteArrays(data, lengthBOM, compare, 0, compare.length)) {
                    // dosn't match so ignore this encoding as it is unlikely to be correct
                    // even if it does contain valid text data.
                    continue;
                }

                // If we get this far and the lengthBOM is not 0 then we have a match for this encoding.
                if (lengthBOM != 0) {
                    // We know we have a perfect match for this encoding so ditch the rest and return just this one
                    possibleEncodings.clear();
                    possibleEncodings.add(encoding);
                    return possibleEncodings;
                }

                // This is a possible match.
                possibleEncodings.add(encoding);
            } catch (UnsupportedEncodingException uee) {
                log.error("The encoding [" + encoding + "] is not supported by your JVM.");
            } catch (Exception e) {
                // Log the error but carry on with the next encoding
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return possibleEncodings;
    }

    public static Collection getValidEncodings(String[] encodings) {
        Collection c = new ArrayList();
        for (int i = 0; i < encodings.length; i++) {
            if (supportedEncodings.contains(encodings[i])) {
                c.add(encodings[i]);
            }
        }
        return c;
    }

    public static String getDefaultEncoding() {
        return EncodingGuesser.defaultJVMEncoding;
    }

    public static Collection getSupportedEncodings() {
        return supportedEncodings;
    }

    public static Collection setSupportedEncodings(Collection encodings) {
        Collection current = new TreeSet();
        for (Iterator it = supportedEncodings.iterator(); it.hasNext(); ) {
            current.add(it.next());
        }
        if (encodings != null) {
            supportedEncodings.clear();
            for (Iterator it = encodings.iterator(); it.hasNext(); ) {
                supportedEncodings.add(it.next());
            }
        }
        return current;
    }

    public static int getLengthBOM(String encoding, byte[] data) {
        if (!boms.containsKey(encoding)) {
            return 0;
        }
        byte[] bom = (byte[]) boms.get(encoding);
        if (compareByteArrays(bom, 0, data, 0, bom.length)) {
            return bom.length;
        } else {
            return 0;
        }
    }

    public static byte[] getByteArraySubArray(byte[] a, int offset, int length) {
        if ((offset + length > a.length)) {
            return a;
        }
        byte[] data = new byte[length];
        for (int i = 0; i < length; i++) {
            data[i] = a[offset + i];
        }
        return data;
    }

    public static boolean compareByteArrays(byte[] a, int aOffset, byte[] b, int bOffset, int length) {
        if ((a.length < aOffset + length) || (b.length < bOffset + length)) {
            // would match beyond one of the arrays
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (a[aOffset + i] != b[bOffset + i]) {
                return false;
            }
        }
        return true;
    }

}
