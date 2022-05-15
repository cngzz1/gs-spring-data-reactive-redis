package hello;

import org.jetbrains.annotations.Contract;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class CoffeeController {
	private final ReactiveRedisOperations<String, Coffee> coffeeOps;

	@Contract(pure = true)
	CoffeeController(ReactiveRedisOperations<String, Coffee> coffeeOps) {
		this.coffeeOps = coffeeOps;
	}

	@GetMapping("/coffees")
	public Flux<Coffee> all() {
		return coffeeOps.keys("*")
				.flatMap(coffeeOps.opsForValue()::get);
	}
}
