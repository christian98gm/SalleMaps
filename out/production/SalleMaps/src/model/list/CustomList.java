package model.list;

import java.util.*;

public class CustomList implements List {

    private final CustomNode firstNode;
    private int size;

    public CustomList() {
        firstNode = new CustomNode();
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {

        //Null object
        if(o == null) {
            throw new NullPointerException();
        }

        CustomNode currentNode = firstNode;

        for(int i = 0; i < size; i++) {
            currentNode = currentNode.getNextNode();
            if(currentNode.getObject().equals(o)) {
                return true;
            }
        }

        return false;

    }

    @Override
    public Iterator iterator() {
        return new Iterator() {

            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public Object next() {
                Object object = get(cursor);
                cursor++;
                return object;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    @Override
    public Object[] toArray() {

        Object[] objects = new Object[size];
        CustomNode currentNode = firstNode;

        for(int i = 0; i < size; i++) {
            currentNode = currentNode.getNextNode();
            objects[i] = currentNode.getObject();
        }

        return objects;

    }

    @Override
    public boolean add(Object o) {

        //Null object
        if(o == null) {
            throw new NullPointerException();
        }

        CustomNode currentNode = firstNode;

        //Search object
        for(int i = 0; i < size; i++) {
            currentNode = currentNode.getNextNode();
            if(currentNode.getObject().equals(o)) {
                return false;   //Exists
            }
        }

        //Not exists
        CustomNode customNode = new CustomNode();
        customNode.setObject(o);
        currentNode.setNextNode(customNode);
        size++;

        return true;

    }

    @Override
    public boolean remove(Object o) {

        //Null object
        if(o == null) {
            throw new NullPointerException();
        }

        CustomNode currentNode = firstNode;

        //Search object
        for(int i = 0; i < size; i++) {

            CustomNode nextNode = currentNode.getNextNode();

            if(nextNode.getObject().equals(o)) {     //Exists
                currentNode.setNextNode(nextNode.getNextNode());
                size--;
                return true;
            }

            currentNode = nextNode;

        }

        return false;   //Not exists

    }

    @Override
    public boolean addAll(Collection c) {

        //Null collection
        if(c == null) {
            throw new NullPointerException();
        }

        boolean somethingAdded = false;

        //Add object
        for(Object object : c) {
            somethingAdded = somethingAdded | add(object);
        }

        return somethingAdded;

    }

    @Override
    public boolean addAll(int index, Collection c) {

        //Null collection
        if(c == null) {
            throw new NullPointerException();
        }

        //Invalid index
        if(index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        //addAll case
        if(index == size) {
            return addAll(c);
        }

        //Move to requested index
        CustomNode currentNode = firstNode;
        for(int i = 0; i < index; i++) {
            currentNode = currentNode.getNextNode();
        }

        boolean somethingAdded = false;

        //Check if any object to add exists
        for(Object object : c) {
            if(!contains(object)) {
                CustomNode customNode = new CustomNode();
                customNode.setObject(object);
                customNode.setNextNode(currentNode.getNextNode());
                currentNode.setNextNode(customNode);
                currentNode = customNode;
                somethingAdded = true;
                size++;
            }
        }

        return somethingAdded;

    }

    @Override
    public void clear() {
        firstNode.setNextNode(null);
        size = 0;
    }

    @Override
    public Object get(int index) {

        //Invalid index
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        CustomNode currentNode = firstNode;

        //Search object with i == index
        for(int i = 0; i < size; i++) {
            currentNode = currentNode.getNextNode();
            if(i == index) {
                return currentNode.getObject();
            }
        }

        return null;    //Never happens

    }

    @Override
    public Object set(int index, Object element) {

        //Invalid index
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        //Null object
        if(element == null) {
            throw new NullPointerException();
        }

        CustomNode currentNode = firstNode;

        //Swap if exists
        if(contains(element)) {

            //Check if has same index
            if(indexOf(element) == index) {
                return element;
            }

            Object to = get(index);

            for(int i = 0; i < size; i++) {
                currentNode = currentNode.getNextNode();
                if(currentNode.getObject().equals(element)) {
                    currentNode.setObject(to);
                }
                if(i == index) {
                    currentNode.setObject(element);
                }
            }

            return to;

        }

        //Search object with i == index
        for(int i = 0; i < size; i++) {
            currentNode = currentNode.getNextNode();
            if(i == index) {
                Object object = currentNode.getObject();
                currentNode.setObject(element);
                return object;
            }
        }

        return null;    //Only null when already exists

    }

    @Override
    public void add(int index, Object element) {

        //Invalid index
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        //Null object
        if(element == null) {
            throw new NullPointerException();
        }

        //Check if exists in list
        if(contains(element)) {
            return;
        }

        CustomNode currentNode = new CustomNode();

        //Add object if not exists
        for(int i = 0; i < size; i++) {
            currentNode = currentNode.getNextNode();
            if(i == index) {
                CustomNode customNode = new CustomNode();
                customNode.setObject(element);
                customNode.setNextNode(currentNode.getNextNode());
                currentNode.setNextNode(customNode);
                size++;
                break;
            }
        }

    }

    @Override
    public Object remove(int index) {

        //Invalid index
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        CustomNode currentNode = firstNode;

        //Search object
        for(int i = 0; i < size; i++) {

            CustomNode nextNode = currentNode.getNextNode();

            if(i == index) {
                currentNode.setNextNode(nextNode.getNextNode());
                size--;
                return true;
            }

            currentNode = nextNode;

        }

        return false;   //Not exists

    }

    @Override
    public int indexOf(Object o) {

        //Null object
        if(o == null) {
            throw new NullPointerException();
        }

        CustomNode currentNode = firstNode;

        for(int i = 0; i < size; i++) {
            currentNode = currentNode.getNextNode();
            if(currentNode.getObject().equals(o)) {
                return i;
            }
        }

        return -1;

    }

    @Override
    public int lastIndexOf(Object o) {
        return indexOf(o);
    }

    @Override
    public ListIterator listIterator() {
        return new ListIterator() {

            private int cursor = 0;
            private int lastReturned = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public Object next() {
                Object object = get(cursor);
                lastReturned = cursor;
                cursor++;
                return object;
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            public Object previous() {
                cursor--;
                lastReturned = cursor;
                return get(cursor);
            }

            @Override
            public int nextIndex() {
                return cursor;
            }

            @Override
            public int previousIndex() {
                return cursor - 1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(Object o) {
                CustomList.this.set(lastReturned, o);
            }

            @Override
            public void add(Object o) {
                throw new UnsupportedOperationException();
            }

        };
    }

    @Override
    public ListIterator listIterator(int index) {

        //Invalid index
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        return new ListIterator() {

            private int cursor = index;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public Object next() {
                Object object = get(cursor);
                cursor++;
                return object;
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            public Object previous() {
                cursor--;
                return get(cursor);
            }

            @Override
            public int nextIndex() {
                return cursor;
            }

            @Override
            public int previousIndex() {
                return cursor - 1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(Object o) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void add(Object o) {
                throw new UnsupportedOperationException();
            }

        };

    }

    @Override
    public List subList(int fromIndex, int toIndex) {

        //Invalid indices
        if(fromIndex < 0 || fromIndex >= size || toIndex < 0 || toIndex >= size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        CustomList list = new CustomList();
        CustomNode currentNode = firstNode;

        //Add objects
        for(int i = 0; i <= toIndex; i++) {
            currentNode = currentNode.getNextNode();
            if(i >= fromIndex) {
                list.add(currentNode.getObject());
            }
        }

        return list;

    }

    @Override
    public boolean retainAll(Collection c) {

        //Null collection
        if(c == null) {
            throw new NullPointerException();
        }

        boolean somethingRemoved = false;
        CustomNode currentNode = firstNode;

        while(currentNode.getNextNode() != null) {
            CustomNode nextNode = currentNode.getNextNode();
            if(!c.contains(nextNode.getObject())) {
                currentNode.setNextNode(nextNode.getNextNode());
                size--;
                somethingRemoved = true;
            } else {
                currentNode = nextNode;
            }
        }

        return somethingRemoved;

    }

    @Override
    public boolean removeAll(Collection c) {

        //Null collection
        if(c == null) {
            throw new NullPointerException();
        }

        boolean somethingRemoved = false;

        //Remove object
        for(Object object : c) {
            somethingRemoved = somethingRemoved || remove(object);
        }

        return somethingRemoved;

    }

    @Override
    public boolean containsAll(Collection c) {

        //Null collection
        if(c == null) {
            throw new NullPointerException();
        }

        for(Object object : c) {
            if(!contains(object)) {
                return false;
            }
        }

        return true;

    }

    @Override
    public Object[] toArray(Object[] a) {

        //Null array
        if(a == null) {
            throw new NullPointerException();
        }

        int occupiedIndex;
        int duplicatedObjects = 0;

        //Get total occupied length
        for(occupiedIndex = 0; occupiedIndex < a.length; occupiedIndex++) {
            if(a[occupiedIndex] == null) {
                break;
            }
            if(contains(a[occupiedIndex])) {
                duplicatedObjects++;
            }
        }

        Object[] objects = a;
        int missingObjects = size - duplicatedObjects;

        //Check if has enough length
        if(a.length - occupiedIndex < missingObjects) {
            objects = new Object[occupiedIndex - duplicatedObjects + size];
            System.arraycopy(a, 0, objects, 0, occupiedIndex);
        }

        //Add list components
        CustomNode currentNode = firstNode;
        for(int i = 0; i < size; i++) {

            currentNode = currentNode.getNextNode();
            Object object = currentNode.getObject();

            //Check if exists
            for(int j = 0; j < objects.length; j++) {
                if(object.equals(objects[j])) {
                    break;
                }
                if(objects[j] == null) {
                    objects[j] = object;
                }
            }

        }

        return objects;

    }

}
