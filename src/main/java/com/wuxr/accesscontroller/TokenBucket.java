package com.wuxr.accesscontroller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * 令牌桶
 */
public class TokenBucket {
    private long lastRefillTime;
    private long bucketLimit;
    private long currentTokensRemaining;
    private long intervalInMills;
    private long permitPerInterval;
    private static final Logger logger = LogManager.getLogger(com.wuxr.accesscontroller.TokenBucket.class);


    /**
     * @param bucketLimit   桶最大容量
     * @param permitPerSec  每秒产生令牌数量
     */
    public TokenBucket(long bucketLimit,long permitPerInterval,long intervalInMills){
        this.lastRefillTime=System.currentTimeMillis();
        this.bucketLimit= bucketLimit;
        this.currentTokensRemaining=bucketLimit;
        this.permitPerInterval=permitPerInterval;
        this.intervalInMills=intervalInMills;
        logger.trace("bucket initialized");
    }
    public synchronized boolean  getToken(){

        logger.trace("currentTokenReming:"+this.currentTokensRemaining);

        long currentTime= System.currentTimeMillis();
        long intervalSinceLast =currentTime - this.lastRefillTime;


        //根据间隔时间计算令牌数量
        if (intervalSinceLast > this.intervalInMills) {
            this.currentTokensRemaining = bucketLimit;
            this.lastRefillTime=currentTime;
        } else {
            long grantedTokens =  intervalSinceLast * permitPerInterval/ intervalInMills;
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
