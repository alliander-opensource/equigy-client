package com.alliander.equigy.client.oauth.sensitive;

import org.junit.Test;

import java.net.http.HttpRequest;
import java.nio.ByteBuffer;
import java.util.concurrent.Flow;

import static org.assertj.core.api.Assertions.assertThat;

public class SensitiveBufferTest {

    @Test
    public void whenUnsafeMappedThenMapperValueIsReturned() {
        final ByteBuffer in = ByteBuffer.allocate(1).put((byte) 20).flip();

        final int out = new SensitiveBuffer(in).unsafeMap(b -> b.get() + 4);

        assertThat(out).isEqualTo(24);
    }

    @Test
    public void whenUnsafeMappedThenInputByteBufferIsCleared() {
        final ByteBuffer in = ByteBuffer.allocate(1).put((byte) 20).flip();

        new SensitiveBuffer(in).unsafeMap(b -> b.get() + 4);

        assertThat(in.get(0)).isEqualTo((byte) 0);
    }

    @Test
    public void whenMappedThenMapperValueIsReturned() {
        final ByteBuffer in = ByteBuffer.allocate(1).put((byte) 70).flip();

        final byte out = new SensitiveBuffer(in).map(b -> ByteBuffer.allocate(1).put((byte) (b.get() + 4)).flip())
                .unsafeMap(ByteBuffer::get);

        assertThat(out).isEqualTo((byte) 74);
    }

    @Test
    public void whenMappedThenInputByteBufferIsCleared() {
        final ByteBuffer in = ByteBuffer.allocate(1).put((byte) 70).flip();

        new SensitiveBuffer(in).map(b -> ByteBuffer.allocate(1).put((byte) (b.get() + 4)));

        assertThat(in.get(0)).isEqualTo((byte) 0);
    }

    @Test
    public void whenMappedAsBodyPublisherThenBodyPublisherHasContentLength() {
        final ByteBuffer in = ByteBuffer.allocate(1).put((byte) 150).flip();

        final long contentLength = new SensitiveBuffer(in).mapAsBodyPublisher(HttpRequest.BodyPublisher::contentLength);

        assertThat(contentLength).isEqualTo(1);
    }

    @Test
    public void whenMappedAsBodyPublisherThenMapperValueIsReturned() {
        final ByteBuffer in = ByteBuffer.allocate(1).put((byte) 150).flip();

        final byte out = new SensitiveBuffer(in).mapAsBodyPublisher(bp -> {
            final MockSubscriber subscriber = new MockSubscriber();
            bp.subscribe(subscriber);

            assertThat(subscriber.completed).isTrue();
            assertThat(subscriber.throwable).isNull();
            return subscriber.item.get();
        });

        assertThat(out).isEqualTo((byte) 150);
    }

    class MockSubscriber implements Flow.Subscriber<ByteBuffer> {
        ByteBuffer item;
        Throwable throwable;
        boolean completed;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            subscription.request(1);
        }

        @Override
        public void onNext(ByteBuffer item) {
            this.item = item;
        }

        @Override
        public void onError(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public void onComplete() {
            completed = true;
        }
    }
}
