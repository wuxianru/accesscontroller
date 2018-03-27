package com.wuxr.accesscontroller;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;



class AccessControllerTest {

    EHCacheAdapter ea=new EHCacheAdapter();
    AccessController ac = new AccessController();
    @BeforeEach
    public void init(){

        //init Cache
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("testCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, TokenBucket.class,
                                ResourcePoolsBuilder.heap(100))
                                .build())
                .build(true);

        Cache<String, TokenBucket> cache
                = cacheManager.getCache("testCache", String.class, TokenBucket.class);

        ea.setEhcache(cache);
        ac.setBucketLimit(100);
        ac.setPermitPerSec(1);
        ac.setCacheAdapter(ea);
        ac.setControllerType("IP");






    }

    @Test
    void testTest() {
        assert(true);
    }

    /**
     *
     * @throws InterruptedException
     */
    @Test
    void accessControl() throws InterruptedException {
        for(int j=0;j<10;j++) {
            final int fj=j;
            for (int i = 0; i < 101; i++)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        assertTrue(ac.accessControl("ip" + fj));
                    }
                }).start();
        }
//        Thread.sleep(30);
//        assertFalse(ac.accessControl("1"));
//        Thread.sleep(3000);
//        assertTrue(ac.accessControl("1"));

        Thread.sleep(10000);
    }

    @AfterEach
    public void finishe(){

    }
}