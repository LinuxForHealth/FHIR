/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core;

import java.io.File;

/**
 * CachedObjectHolder is a generic type which provides common behavior for a file-based object
 * stored within a cache.  
 * 
 * @author padams
 */
public class CachedObjectHolder<T> {
    private String fileName;
    private long lastModified;
    private T cachedObject;

    public CachedObjectHolder(String fileName, T cachedObject) {
        setFileName(fileName);
        File f = new File(fileName);
        setLastModified(f.lastModified());
        setCachedObject(cachedObject);
    }
    
    public CachedObjectHolder(T cachedObject) {
        setCachedObject(cachedObject);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public T getCachedObject() {
        return cachedObject;
    }

    public void setCachedObject(T cachedObject) {
        this.cachedObject = cachedObject;
    }

    /**
     * @return true iff the file from which the cached object was initially derived has been 
     * modified since the object was cached.
     */
    public boolean isStale() {
        if (fileName != null) {
            File f = new File(fileName);
            return !f.exists() || f.lastModified() > getLastModified();
        }
        
        return false;
    }
}
