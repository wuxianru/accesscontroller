package com.wuxr.accesscontroller;


import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;


public class EHCacheAdapter implements CacheAdapter{

    private Cache<String,TokenBucket> cache;

    public void setEhcache(Cache<String,TokenBucket> cache){
        this.cache=cache;

    }



    @Override
    public TokenBucket getBucket(String key) {
        return cache.get(key);
    }

    @Override
    public void putBucket(String typeKey, TokenBucket tokenBucket) {
        cache.put(typeKey,tokenBucket);

    }
}
