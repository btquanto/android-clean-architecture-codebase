package com.theitfox.architecture.data.cache;

import android.content.Context;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

import rx.Observable;

/**
 * Created by btquanto on 14/09/2016.
 * <p>
 * DiskCache is the abstract wrapper for caching using DiskLruCache
 *
 * @param <T> the type of object that would be cached
 */
public abstract class DiskCache<T> {

    /**
     * The number of values per cache entry. Must be positive.
     * There's usually no need to change this constant
     */
    protected static final int VALUE_COUNT = 1;

    /**
     * The app version
     */
    protected static final int APP_VERSION = 1;

    /**
     * The cache's parent directory.
     */
    protected static final String CACHE_DIR = "IrisStudio";

    private DiskLruCache mDiskCache;
    private File diskCacheDir;
    private int diskCacheSize;

    /**
     * Instantiates a new Disk cache.
     *
     * @param cacheSubDir   the cache sub-directory
     * @param diskCacheSize the maximum size that is allowed. This is a soft limit. Read DiskLruCache documentation for more information
     */
    public DiskCache(Context context, String cacheSubDir, int diskCacheSize) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            this.diskCacheDir = new File(Environment.getExternalStorageDirectory(),
                    File.separator + CACHE_DIR + File.separator + cacheSubDir);
        } else {
            this.diskCacheDir = new File(context.getCacheDir(),
                    File.separator + CACHE_DIR + File.separator + cacheSubDir);
        }
        this.diskCacheSize = diskCacheSize;
    }

    /* Public methods and functions */

    /**
     * Put an entry into disk cache using observable
     *
     * @param key   the key
     * @param entry the entry
     * @return the observable
     */
    public Observable<T> putWithObservable(String key, T entry) {
        return getDiskLruCache()
                .map(diskLruCache -> {
                    put(diskLruCache, key, entry);
                    return entry;
                });
    }

    /**
     * Get an entry from disk cache using observable
     *
     * @param key the key
     * @return the with observable
     */
    public Observable<T> getWithObservable(String key) {
        return getDiskLruCache()
                .map(diskLruCache -> get(diskLruCache, key));
    }

    /**
     * Check if the cache contains a snapshot for a specific key
     *
     * @param diskLruCache the disk lru cache
     * @param key          the key
     * @return the boolean
     */
    protected boolean contains(DiskLruCache diskLruCache, String key) {
        boolean isKeyContained = false;

        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = diskLruCache.get(key);
            isKeyContained = snapshot != null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }

        return isKeyContained;
    }

    /**
     * Clear all cached data
     *
     * @param diskLruCache the disk lru cache
     */
    protected void clear(DiskLruCache diskLruCache) {
        try {
            diskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Observable<DiskLruCache> getDiskLruCache() {
        if (mDiskCache != null) {
            return Observable.just(mDiskCache);
        } else {
            return Observable.create(subscriber -> {
                try {
                    mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, diskCacheSize);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(mDiskCache);
            });
        }
    }

    /**
     * Synchronously put data into cache
     * This method must be overridden by subclasses
     * Since each type of data may have a different cache mechanism
     *
     * @param diskLruCache the diskLruCache
     * @param key          the key
     * @param entry        the entry
     */
    protected abstract void put(DiskLruCache diskLruCache, String key, T entry);

    /**
     * Synchronously get data from cache
     * This method must be overridden by subclasses
     * Since each type of data may have a different cache mechanism
     *
     * @param diskLruCache the diskLruCache
     * @param key          the key
     * @return the t
     */
    protected abstract T get(DiskLruCache diskLruCache, String key);
}
