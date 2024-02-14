package com.example.demo;


public class CursorArray<T extends Comparable<T>> {

    Node<T>[] cursorArray;
    private int capacity = 50;


    public CursorArray(int capacity) {
        super();
        this.capacity = capacity;
        cursorArray = new Node[capacity];
        initialization();
    }


    public CursorArray() {
        cursorArray = new Node[capacity];
        initialization();
    }

    public void setCapacity(int capacity) {

        this.capacity = capacity;
    }

    private void initialization() {
        for (int i = 0; i < cursorArray.length - 1; i++)
            cursorArray[i] = new Node<T>(null, i + 1);

        cursorArray[cursorArray.length - 1] = new Node<>(null, 0);
    }

    public int malloc() {
        int i = cursorArray[0].getNext();
        cursorArray[0].setNext(cursorArray[i].getNext());
        return i;
    }

    public void free(int p) {
        cursorArray[p] = new Node<T>(null, cursorArray[0].getNext());
        cursorArray[0].setNext(p);
    }

    public boolean isNull(int i) {
        return cursorArray[i] == null;
    }

    public boolean isEmpty(int l) {
        return cursorArray[l].getNext() == 0;
    }

    public boolean isLast(int p) {
        return cursorArray[p].getNext() == 0;
    }

    public int createList() {
        int p = malloc();
        if (p != 0)
            cursorArray[p].setNext(0);
        else
            p = -1;
        return p;
    }

    public void insert(T data, int l) {
        int i, j;
        for (j = l, i = cursorArray[l].getNext(); i != 0 && !isNull(i) && !isLast(i)
                && cursorArray[i].getData().compareTo(data) < 0; j = i, i = cursorArray[i].getNext())
            ;

        int p = malloc();
        if (p != 0) {
            if (i == l) {
                cursorArray[p] = new Node<>(data, 0);
                cursorArray[l].setNext(p);
            }
            else if (i == 0) {
                cursorArray[j].setNext(p);
                cursorArray[p] = new Node<T>(data, 0);
            }
            else {
                cursorArray[j].setNext(p);
                cursorArray[p] = new Node<T>(data, i);
            }
        }

    }

    public void insertAtHead(T data, int l) {
        if (isNull(l))
            return;
        int p = malloc();
        if (p != 0) {
            cursorArray[p] = new Node<>(data, cursorArray[l].getNext());
            cursorArray[l].setNext(p);
        } else {
            System.out.println("STACK OVERFLOW!!!");
        }

    }

    public int find(T data, int l) {
        while (!isNull(l) && !isEmpty(l)) {
            l = cursorArray[l].getNext();
            if (cursorArray[l].getData().equals(data))
                return l;
        }
        return -1; // not found
    }

    public int findPrevious(T data, int l) {
        while (!isNull(l) && !isEmpty(l)) {
            if (cursorArray[cursorArray[l].getNext()].getData().equals(data))
                return l;
            l = cursorArray[l].getNext();
        }
        return -1; // not found
    }

    public Node<T> delete(T data, int l) {
        int p = findPrevious(data, l);
        if (p != -1) {
            int c = cursorArray[p].getNext();
            Node<T> temp = cursorArray[c];
            cursorArray[p].setNext(temp.getNext());
            free(c);
        }
        return null;
    }

    public T deleteHead(int l) {
        if (!isNull(l) && !isEmpty(l)) {
            int c = cursorArray[l].getNext();
            Node<T> temp = cursorArray[c];
            cursorArray[l].setNext(temp.getNext());
            free(c);
            return temp.getData();
        }
        return null;

    }

    public T getHead(int l) {

        int c = cursorArray[l].getNext();
        Node<T> temp = cursorArray[c];

        return temp.getData();
    }

    public String traverse(int l) {
        String s = "HEAD -> ";
        while (!isLast(l) && !isNull(l)) {
            l = cursorArray[l].getNext();
            s += cursorArray[l].toString() + " -> ";
        }
        s += "NULL";
        return s;
    }


}

