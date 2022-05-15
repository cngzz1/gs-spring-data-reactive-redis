package hello;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;

public interface ICoffeeConfiguration<E> {
    ReactiveRedisOperations<String, E> redisOperations(ReactiveRedisConnectionFactory factory);
}
