package com.alliander.equigy.client.oauth.sensitive;

import java.net.http.HttpRequest;
import java.nio.ByteBuffer;
import java.util.concurrent.Flow;

class SensitiveBodyPublisher implements HttpRequest.BodyPublisher {
    private final ByteBuffer bufferedRequest;
    private volatile boolean sent = false;

    public SensitiveBodyPublisher(ByteBuffer bufferedRequest) {
        this.bufferedRequest = bufferedRequest;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super ByteBuffer> subscriber) {
        subscriber.onSubscribe(new Flow.Subscription() {
            @Override
            public void request(long n) {
                if (!sent) {
                    sent = true;
                    subscriber.onNext(bufferedRequest);
                    subscriber.onComplete();
                }
            }

            @Override
            public void cancel() {
            }
        });
    }

    @Override
    public long contentLength() {
        return bufferedRequest.remaining();
    }
}
