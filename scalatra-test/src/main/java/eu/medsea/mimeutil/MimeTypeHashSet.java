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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

class MimeTypeHashSet implements Set<Object>, Collection<Object> {

    private Set hashSet = new LinkedHashSet();

    MimeTypeHashSet() {
    }

    MimeTypeHashSet(final Collection collection) {
        addAll(collection);
    }

    public boolean add(final Object arg0) {
        if (arg0 == null) {
            // We don't allow null
            return false;
        }
        if ((arg0 instanceof MimeType)) {
            // Add a MimeType
            if (contains(arg0)) {
                // We already have an entry so get it and update the specificity
                updateSpecificity((MimeType) arg0);
            }
            MimeUtil.addKnownMimeType((MimeType) arg0);
            return hashSet.add(arg0);

        } else if (arg0 instanceof Collection) {
            // Add a collection
            return addAll((Collection) arg0);
        } else if (arg0 instanceof String) {
            // Add a string representation of a mime type that could be a comma separated list
            String[] mimeTypes = ((String) arg0).split(",");
            boolean added = false;
            for (int i = 0; i < mimeTypes.length; i++) {
                try {
                    if (add(new MimeType(mimeTypes[i]))) {
                        added = true;
                    }
                } catch (Exception e) {
                    // Ignore this as it's not a type we can use
                }
            }
            return added;
        } else if (arg0 instanceof String[]) {
            // Add a String array of mime types each of which can be a comma separated list of mime types
            boolean added = false;
            String[] mimeTypes = (String[]) arg0;
            for (int i = 0; i < mimeTypes.length; i++) {
                String[] parts = mimeTypes[i].split(",");
                for (int j = 0; j < parts.length; j++) {
                    try {
                        if (add(new MimeType(parts[j]))) {
                            added = true;
                        }
                    } catch (Exception e) {
                        // Ignore this as it's not a type we can use
                    }
                }
            }
            return added;
        }
        // Can't add this type
        return false;
    }

    public boolean addAll(final Collection arg0) throws NullPointerException {
        if (arg0 == null) {
            throw new NullPointerException();
        }
        boolean added = false;
        for (Iterator it = arg0.iterator(); it.hasNext(); ) {
            try {
                if (add(it.next())) {
                    added = true;
                }
            } catch (Exception e) {
                // Ignore this entry as it's not a types that can be turned into MimeTypes
            }
        }
        return added;
    }

    public void clear() {
        hashSet.clear();
    }

    public boolean contains(final Object o) {
        if (o instanceof MimeType) {
            return hashSet.contains(o);
        } else if (o instanceof Collection) {
            return containsAll((Collection) o);
        } else if (o instanceof String) {
            String[] parts = ((String) o).split(",");
            for (int i = 0; i < parts.length; i++) {
                if (!contains(new MimeType(parts[i]))) {
                    return false;
                }
            }
            return true;
        } else if (o instanceof String[]) {
            String[] mimeTypes = (String[]) o;
            for (int i = 0; i < mimeTypes.length; i++) {
                String[] parts = mimeTypes[i].split(",");
                for (int j = 0; j < parts.length; j++) {
                    if (!contains(new MimeType(parts[j]))) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean containsAll(final Collection arg0) {
        if (arg0 == null) {
            throw new NullPointerException();
        }
        for (Iterator it = arg0.iterator(); it.hasNext(); ) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return hashSet.isEmpty();
    }

    public Iterator iterator() {
        return hashSet.iterator();
    }

    public boolean remove(final Object o) {
        boolean removed = false;
        if (o == null) {
            return removed;
        }
        if (o instanceof MimeType) {
            return hashSet.remove(o);
        } else if (o instanceof String) {
            String[] parts = ((String) o).split(",");
            for (int i = 0; i < parts.length; i++) {
                if (remove(new MimeType(parts[i]))) {
                    removed = true;
                }
            }
        } else if (o instanceof String[]) {
            String[] mimeTypes = (String[]) o;
            for (int i = 0; i < mimeTypes.length; i++) {
                String[] parts = mimeTypes[i].split(",");
                for (int j = 0; j < parts.length; j++) {
                    if (remove(new MimeType(parts[j]))) {
                        removed = true;
                    }
                }
            }
        } else if (o instanceof Collection) {
            return removeAll((Collection) o);
        }
        return removed;
    }

    public boolean removeAll(final Collection arg0) {
        if (arg0 == null) {
            throw new NullPointerException();
        }
        boolean removed = false;
        for (Iterator it = ((Collection) arg0).iterator(); it.hasNext(); ) {
            if (remove(it.next())) {
                removed = true;
            }
        }
        return removed;
    }

    public boolean retainAll(final Collection arg0) {
        if (arg0 == null) {
            throw new NullPointerException();
        }
        // Make sure our collection is a real collection of MimeType(s)
        Collection c = new MimeTypeHashSet(arg0);
        return hashSet.retainAll(c);
    }

    public int size() {
        return hashSet.size();
    }

    public Object[] toArray() {
        return hashSet.toArray();
    }

    public Object[] toArray(final Object[] arg0) {
        return hashSet.toArray(arg0);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (Iterator it = iterator(); it.hasNext(); ) {
            buf.append(((MimeType) it.next()).toString());
            if (it.hasNext()) {
                buf.append(",");
            }
        }
        return buf.toString();
    }

    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        Collection c = new MimeTypeHashSet();
        c.add(o);
        return match(c);
    }

    private boolean match(final Collection c) {
        if (this.size() != c.size()) {
            return false;
        }
        MimeType[] mt = (MimeType[]) c.toArray(new MimeType[c.size()]);

        for (int i = 0; i < mt.length; i++) {
            if (!this.contains(mt[i])) {
                return false;
            }
        }
        return true;
    }

    private void updateSpecificity(final MimeType o) {
        MimeType mimeType = get(o);
        int specificity = mimeType.getSpecificity() + o.getSpecificity();
        mimeType.setSpecificity(specificity);
        o.setSpecificity(specificity);
    }

    private MimeType get(MimeType mimeType) {
        for (Iterator it = hashSet.iterator(); it.hasNext(); ) {
            MimeType mt = (MimeType) it.next();
            if (mt.equals(mimeType)) {
                return mt;
            }
        }
        return null;
    }

    public Collection matches(String pattern) {
        Collection c = new MimeTypeHashSet();
        for (Iterator it = iterator(); it.hasNext(); ) {
            MimeType mimeType = (MimeType) it.next();
            if (mimeType.toString().matches(pattern)) {
                c.add(mimeType);
            }
        }
        return c;
    }

}
