package com.wuxr.accesscontroller;

public class TokenBucket {
    private long lastRefillTime;
    private long bucketLimit;
    private long currentTokensRemaining;
    private long intervalInMills;
    private long intervalPerPermit;


    public TokenBucket(long initTime,long bucketLimit,long intervalInMills,long intervalPerPermit){
        this.lastRefillTime=initTime;
        this.bucketLimit= bucketLimit;
        this.currentTokensRemaining=bucketLimit;
        this.intervalInMills=intervalInMills;
        this.intervalPerPermit=intervalPerPermit;
    }
    public synchronized boolean  getToken(){

        long currentTime= System.currentTimeMillis();
        long intervalSinceLast =currentTime - this.lastRefillTime;
        this.lastRefillTime=currentTime;

        //根据间隔时间计算令牌数量
        if (intervalSinceLast > this.intervalInMills) {
            this.currentTokensRemaining = bucketLimit;
        } else {
            long grantedTokens =  intervalSinceLast / intervalPerPermit;
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
