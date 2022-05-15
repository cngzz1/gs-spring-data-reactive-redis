package hello;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;

public interface ICoffeeConfiguration<K, E> {
    ReactiveRedisOperations<K, E> redisOperations(ReactiveRedisConnectionFactory factory);
}
