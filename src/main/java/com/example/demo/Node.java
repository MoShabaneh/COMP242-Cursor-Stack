package com.example.demo;

public class Node<T extends Comparable<T>> {

    private T data;
    private int next;

    public Node(T data, int next) {
        super();
        this.data = data;
        this.next = next;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getNext() {
        return next;
    }
}
