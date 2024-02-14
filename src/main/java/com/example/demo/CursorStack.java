package com.example.demo;


public class CursorStack<T extends Comparable<T>> implements Stackable<T> {

    private CursorArray<T> cursorArray = new CursorArray<>(250);
    private int List;

    public CursorStack() {

        setList(cursorArray.createList());
    }

    public int getList() {
        return List;
    }

    public void setList(int List) {
        this.List = List;
    }

    @Override
    public void push(T data)  {
        cursorArray.insertAtHead(data, List);
    }

    @Override
    public T pop() {
        return cursorArray.deleteHead(List);
    }

    @Override
    public T peek() {
        return cursorArray.getHead(List);
    }

    @Override
    public boolean isEmpty() {
        return cursorArray.isEmpty(List);
    }

    @Override
    public void isClear() {
        cursorArray = new CursorArray<>(List);
    }

}
