package com.wuxr.accesscontroller;

public interface CacheAdapter {
    public TokenBucket getBucket(String key);

    public void putBucket(String typeKey,TokenBucket tokenBucket);
}
