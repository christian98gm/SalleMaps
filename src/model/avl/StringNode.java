package model.avl;

public class StringNode {

    private String string;
    private int index;
    private StringNode leftChild;
    private StringNode rightChild;

    public StringNode() {}

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public StringNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(StringNode leftChild) {
        this.leftChild = leftChild;
    }

    public boolean hasLeftChild() {
        return leftChild != null;
    }

    public StringNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(StringNode rightChild) {
        this.rightChild = rightChild;
    }

    public boolean hasRightChild() {
        return rightChild != null;
    }

    public boolean haveChild() {
        return leftChild != null || rightChild != null;
    }

}