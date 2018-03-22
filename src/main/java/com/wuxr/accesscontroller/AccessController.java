package com.wuxr.accesscontroller;


public class AccessController {

    private CacheAdapter cacheAdapter;

    private long bucketLimit;

    private String controllerType;

    private long intervalInMills;

    public CacheAdapter getCacheAdapter() {
        return cacheAdapter;
    }

    public void setCacheAdapter(CacheAdapter cacheAdapter) {
        this.cacheAdapter = cacheAdapter;
    }

    public long getBucketLimit() {
        return bucketLimit;
    }

    public void setBucketLimit(long bucketLimit) {
        this.bucketLimit = bucketLimit;
    }

    public String getControllerType() {
        return controllerType;
    }

    public void setControllerType(String controllerType) {
        this.controllerType = controllerType;
    }

    public long getIntervalInMills() {
        return intervalInMills;
    }

    public void setIntervalInMills(long intervalInMills) {
        this.intervalInMills = intervalInMills;
    }

    public long getIntervalPerPermit() {
        return intervalPerPermit;
    }

    public void setIntervalPerPermit(long intervalPerPermit) {
        this.intervalPerPermit = intervalPerPermit;
    }

    private long intervalPerPermit;


    public boolean accessControl(String controlKey) {
        String controlTypeKey=this.controllerType+controlKey;
        TokenBucket tb = cacheAdapter.getBucket(controlTypeKey);

        if (tb == null) {
            synchronized (cacheAdapter) {
                tb=cacheAdapter.getBucket(controlTypeKey);
                if (tb == null) {
                    tb = new TokenBucket(System.currentTimeMillis(),
                            this.bucketLimit, this.intervalInMills, this.intervalPerPermit);
                    cacheAdapter.putBucket(this.controllerType+controlKey,tb);
                    return true;
                } else {
                    boolean result=tb.getToken();
                    cacheAdapter.putBucket(this.controllerType+controlKey,tb);
                    return result;
                }
            }

        } else {
            boolean result=tb.getToken();
            cacheAdapter.putBucket(this.controllerType+controlKey,tb);
            return result;
        }

    }
}
