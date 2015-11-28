package main.java.controllers.interfaces;

public interface Option<T> {
	 
    public T getValue();

    default public boolean hasSome() {
            return !hasNone();
    }

    default public boolean hasNone() {
            return !hasSome();
    }
}
