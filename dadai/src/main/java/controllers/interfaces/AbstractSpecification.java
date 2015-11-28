package main.java.controllers.interfaces;

public abstract class AbstractSpecification<T> implements Specification<T> {

		@Override
        public boolean isSatisfiedBy(T candidate) {
			return true;
		}

}
