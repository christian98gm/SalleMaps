package model.hash;

public class OpenHashTable {

    private final static int HASH_SIZE = 7000;

    private HashNode[] hashTable;

    public OpenHashTable() {
        hashTable = new HashNode[HASH_SIZE];
    }

    public boolean add(String key, int value) {

        //Null key
        if(key == null) {
            throw new NullPointerException();
        }

        int index = hash(key);

        //Empty keys - values
        if(hashTable[index] == null) {
            HashNode newNode = new HashNode();
            newNode.setKey(key);
            newNode.setValue(value);
            hashTable[index] = newNode;
            return true;
        }

        //Check if exists
        HashNode currentNode;
        HashNode nextValue = hashTable[index];
        do {
            currentNode = nextValue;
            if(currentNode.getKey().equalsIgnoreCase(key)) {
                return false;
            } else {
                nextValue = nextValue.getNextNode();
            }
        } while(nextValue != null);

        //New key - value
        HashNode newNode = new HashNode();
        newNode.setKey(key);
        newNode.setValue(value);
        currentNode.setNextNode(newNode);
        return true;

    }

    public int getValue(String key) {

        //Null key
        if(key == null) {
            throw new NullPointerException();
        }

        int index = hash(key);

        if(hashTable[index] == null) {
            return -1;
        }

        HashNode currentNode;
        HashNode nextValue = hashTable[index];
        do {
            currentNode = nextValue;
            if(currentNode.getKey().equalsIgnoreCase(key)) {
                return currentNode.getValue();
            } else {
                nextValue = nextValue.getNextNode();
            }
        } while(nextValue != null);

        return -1;

    }

    public boolean containsKey(String key) {

        //Null key
        if(key == null) {
            throw new NullPointerException();
        }

        int index = hash(key);

        if(hashTable[index] == null) {
            return false;
        }

        HashNode currentNode;
        HashNode nextValue = hashTable[index];
        do {
            currentNode = nextValue;
            if(currentNode.getKey().equalsIgnoreCase(key)) {
                return true;
            } else {
                nextValue = nextValue.getNextNode();
            }
        } while(nextValue != null);

        return false;

    }

    private int hash(String key) {
        return Math.abs(key.toLowerCase().hashCode()) % HASH_SIZE;
    }

}