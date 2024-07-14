package com.htmake.htbot.global.generics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class LimitedCapacityDeque<T> {

    private final Deque<T> deque;
    private final Integer maxSize;

    public LimitedCapacityDeque() {
        this.deque = new ArrayDeque<>();
        this.maxSize = null;
    }

    public LimitedCapacityDeque(int maxSize) {
        this.deque = new ArrayDeque<>();

        if (maxSize < 1) {
            throw new RuntimeException("Size cannot be less than 1. Your size is " + maxSize);
        }

        this.maxSize = maxSize;
    }

    public void addFirst(T element) {
        if (maxSize != null && deque.size() >= maxSize) {
            deque.removeLast();
        }

        deque.addFirst(element);
    }

    public void addLast(T element) {
        if (maxSize != null && deque.size() >= maxSize) {
            deque.removeFirst();
        }
        deque.addLast(element);
    }

    public void removeFirst() {
        deque.removeFirst();
    }

    public void removeLast() {
        deque.removeLast();
    }

    public T getFirst() {
        return deque.getFirst();
    }

    public T getLast() {
        return deque.getLast();
    }

    public List<T> findAll() {
        return new ArrayList<>(deque);
    }

    public int size() {
        return deque.size();
    }

    public Integer maxSize() {
        return maxSize;
    }
}
