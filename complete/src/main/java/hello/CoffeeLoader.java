package hello;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
public class CoffeeLoader<E> implements ICoffeeLoader<Coffee<E>> {
	private final ReactiveRedisConnectionFactory factory;
	private final ReactiveRedisOperations<String, Coffee<Object>> coffeeOps;

	public CoffeeLoader(ReactiveRedisConnectionFactory factory, ReactiveRedisOperations<String, Coffee<Object>> coffeeOps) {
		this.factory = factory;
		this.coffeeOps = coffeeOps;
	}

	@CacheEvict
	@PostConstruct
	public void loadData() {
		factory.getReactiveConnection().serverCommands().flushAll().thenMany(
				Flux.just("Jet Black Redis", "Darth Redis", "Black Alert Redis")
						.map(name -> new Coffee<>(UUID.randomUUID().toString(), name))
						.flatMap(coffee -> coffeeOps.opsForValue().set(coffee.getId(), coffee)))
				.thenMany(coffeeOps.keys("*")
						.flatMap(coffeeOps.opsForValue()::get))
				.subscribe(System.out::println);
	}
	@Override
	public Coffee<E> getCoffee(){
		return new Coffee<>();
	}
}
