package fr.benco11.javaquarium.utils;

public final class Pair<A, B> {
    private A a;
    private B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A first() {
        return a;
    }

    public B second() {
        return b;
    }

    public Pair<A, B> setFirst(A a) {
        this.a = a;
        return this;
    }

    public Pair<A, B> setSecond(B b) {
        this.b = b;
        return this;
    }
}
