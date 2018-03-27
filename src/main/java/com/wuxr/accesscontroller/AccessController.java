package com.wuxr.accesscontroller;


public class AccessController {

    private CacheAdapter cacheAdapter;

    private long bucketLimit;

    private String controllerType;

    public void setCacheAdapter(CacheAdapter cacheAdapter) {
        this.cacheAdapter = cacheAdapter;
    }

    public void setBucketLimit(long bucketLimit) {
        this.bucketLimit = bucketLimit;
    }

    public void setControllerType(String controllerType) {
        this.controllerType = controllerType;
    }

    public long getPermitPerSec() {
        return permitPerSec;
    }

    public void setPermitPerSec(long permitPerSec) {
        this.permitPerSec = permitPerSec;
    }

    private long permitPerSec;


    public boolean accessControl(String controlKey) {
        String controlTypeKey=this.controllerType+controlKey;
        TokenBucket tb = cacheAdapter.getBucket(controlTypeKey);

        if (tb == null) {
            synchronized (cacheAdapter) {
                tb=cacheAdapter.getBucket(controlTypeKey);
                if (tb == null) {
                    tb = new TokenBucket(
                            this.bucketLimit,  this.permitPerSec);
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
