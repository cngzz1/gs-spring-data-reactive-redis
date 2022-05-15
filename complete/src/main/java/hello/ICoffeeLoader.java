package hello;

import javax.annotation.PostConstruct;

public interface ICoffeeLoader<E> {
    @PostConstruct
    void loadData();

}
