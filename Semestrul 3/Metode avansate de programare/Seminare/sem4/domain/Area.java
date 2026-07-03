package domain;

@FunctionalInterface
public interface Area<E> {
    double compute(E e);
}
