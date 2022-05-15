package hello;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
public class CoffeeLoader<E> implements ICoffeeLoader<Coffee<E>> {
	private final ReactiveRedisConnectionFactory factory;
	private final ReactiveRedisOperations<String, Coffee<?>> coffeeOps;

	public CoffeeLoader(ReactiveRedisConnectionFactory factory, ReactiveRedisOperations<String, Coffee<?>> coffeeOps) {
		this.factory = factory;
		this.coffeeOps = coffeeOps;
	}

	@Contract("_ -> new")
	private static @NotNull Coffee<?> apply(String name) {
		return new Coffee<>(UUID.randomUUID().toString(), name);
	}


	@PostConstruct
	public void loadData() {
		factory.getReactiveConnection().serverCommands().flushAll().thenMany(
				Flux.just("Jet Black Redis", "Darth Redis", "Black Alert Redis")
						.map(CoffeeLoader::apply)
						.flatMap(this::applyPublisher))
				.thenMany(coffeeOps.keys("*")
						.flatMap(coffeeOps.opsForValue()::get))
				.subscribe(System.out::println);
	}

	@Override
	public Coffee<E> getType() {
		return new Coffee<>();
	}

	private @NotNull Publisher<? extends Boolean> applyPublisher(Coffee<?> coffee) {
		return coffeeOps.opsForValue().set(coffee.getId(), coffee);
	}
}
