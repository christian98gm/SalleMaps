package model.hash;

public class HashNode {

    private String key;
    private int value;
    private HashNode nextNode;

    public HashNode() {}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public HashNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(HashNode nextNode) {
        this.nextNode = nextNode;
    }

}