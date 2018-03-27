package com.wuxr.accesscontroller;


/**
 *
 * 令牌桶
 */
public class TokenBucket {
    private long lastRefillTime;
    private long bucketLimit;
    private long currentTokensRemaining;
    private long intervalInMills;
    private long permitPerSec;


    /**
     * @param bucketLimit   桶最大容量
     * @param permitPerSec  每秒产生令牌数量
     */
    public TokenBucket(long bucketLimit,long permitPerSec){
        this.lastRefillTime=System.currentTimeMillis();
        this.bucketLimit= bucketLimit;
        this.currentTokensRemaining=bucketLimit;
        this.permitPerSec=permitPerSec;
        this.intervalInMills=bucketLimit/permitPerSec*1000;
    }
    public synchronized boolean  getToken(){

        long currentTime= System.currentTimeMillis();
        long intervalSinceLast =currentTime - this.lastRefillTime;

        //根据间隔时间计算令牌数量
        if (intervalSinceLast > this.intervalInMills) {
            this.currentTokensRemaining = bucketLimit;
            this.lastRefillTime=currentTime;
        } else {
            long grantedTokens =  intervalSinceLast * permitPerSec/ 1000;
            if(grantedTokens>0)
                this.lastRefillTime=currentTime;
            currentTokensRemaining = Math.min(grantedTokens + this.currentTokensRemaining, bucketLimit);
        }

        //获取令牌，如无法获取则返回false
        assert this.currentTokensRemaining >= 0;
        if (this.currentTokensRemaining == 0) {
            return false;
        } else {
            this.currentTokensRemaining--;
            return true;
        }
    }
}
