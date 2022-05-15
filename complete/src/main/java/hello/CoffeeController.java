package hello;

import org.jetbrains.annotations.Contract;
import org.reactivestreams.Publisher;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CoffeeController<E> {
	private final ReactiveRedisOperations<String, Coffee<E>> coffeeOps;

	@Contract(pure = true)
	CoffeeController(ReactiveRedisOperations<String, Coffee<E>> coffeeOps) {
		this.coffeeOps = coffeeOps;
	}

	@Cacheable
	@GetMapping("/coffees")
	public Publisher<Coffee<E>> all() {
		return coffeeOps.keys("*")
				.flatMap(coffeeOps.opsForValue()::get);
	}
}
