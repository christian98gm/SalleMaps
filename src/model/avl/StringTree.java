package model.avl;

public class StringTree {

    private StringNode root;
    private int size;

    public StringTree() {
        root = new StringNode();
        size = 0;
    }

    public boolean add(String string) {

        if(size == 0) {
            root.setString(string);
            root.setIndex(size);
            size++;
            return true;
        }

        //Check if exists
        if(containsString(string)) {
            return false;
        }

        StringNode currentNode = root;

        while(currentNode.haveChild()) {
            if(string.compareToIgnoreCase(currentNode.getString()) < 0) {
                if(currentNode.hasLeftChild()) {
                    currentNode = currentNode.getLeftChild();
                } else {
                    break;
                }
            } else {
                if(currentNode.hasRightChild()) {
                    currentNode = currentNode.getRightChild();
                } else {
                    break;
                }
            }

        }

        //Add node
        StringNode newNode = new StringNode();
        newNode.setString(string);
        newNode.setIndex(size);
        if(string.compareToIgnoreCase(currentNode.getString()) < 0) {
            currentNode.setLeftChild(newNode);
        } else {
            currentNode.setRightChild(newNode);
        }

        size++;
        balance(null, root);

        return true;

    }

    public boolean containsString(String string) {

        //Null string
        if(string == null) {
            throw new NullPointerException();
        }

        if(size == 0) {
            return false;
        }

        return contains(root, string);

    }

    private boolean contains(StringNode node, String string) {

        if(node.getString().equalsIgnoreCase(string)) {
            return true;
        }

        if(node.hasLeftChild() && node.hasRightChild()) {
            return contains(node.getLeftChild(), string) || contains(node.getRightChild(), string);
        } else if(node.hasLeftChild()) {
            return contains(node.getLeftChild(), string);
        } else if(node.hasRightChild()) {
            return contains(node.getRightChild(), string);
        }

        return false;

    }

    private int balance(StringNode previousNode, StringNode node) {

        int leftHeight = 0;
        int rightHeight = 0;

        if(node.haveChild()) {
            if(node.hasLeftChild()) {
                leftHeight = balance(node, node.getLeftChild());
            }
            if(node.hasRightChild()) {
                rightHeight = balance(node, node.getRightChild());
            }
        }

        if(Math.abs(leftHeight - rightHeight) == 2) {

            if(leftHeight > rightHeight) {                                                          //LL or LR rotation
                StringNode leftNode = node.getLeftChild();
                if(getHeight(leftNode.getLeftChild()) > getHeight(leftNode.getRightChild())) {      //LL
                    node = leftLeftRotation(previousNode, node);
                } else {                                                                            //LR
                    rightRightRotation(node, node.getLeftChild());
                    node = leftLeftRotation(previousNode, node);
                }
            } else {                                                                                //RR or RL rotation
                StringNode rightNode = node.getRightChild();
                if(getHeight(rightNode.getRightChild()) > getHeight(rightNode.getLeftChild())) {    //RR
                    node = rightRightRotation(previousNode, node);
                } else {                                                                            //RL
                    leftLeftRotation(node, node.getRightChild());
                    node = rightRightRotation(previousNode, node);
                }
            }

            //Get new heights
            leftHeight = node.hasLeftChild() ? getHeight(node.getLeftChild()) : 0;
            rightHeight = node.hasRightChild() ? getHeight(node.getRightChild()) : 0;

        }

        return Integer.max(leftHeight, rightHeight) + 1;

    }

    private int getHeight(StringNode node) {

        if(node == null) {
            return 0;
        }

        int leftHeight = 0;
        int rightHeight = 0;

        if(node.haveChild()) {
            if(node.hasLeftChild()) {
                leftHeight = getHeight(node.getLeftChild());
            }
            if(node.hasRightChild()) {
                rightHeight = getHeight(node.getRightChild());
            }
        }

        return Integer.max(leftHeight, rightHeight) + 1;

    }

    private StringNode leftLeftRotation(StringNode previousNode, StringNode node) {

        StringNode leftChild = node.getLeftChild();

        if(previousNode == null) {
            root = leftChild;
        } else {
            if(previousNode.hasLeftChild() && previousNode.getLeftChild().getIndex() == node.getIndex()) {
                previousNode.setLeftChild(leftChild);
            } else {
                previousNode.setRightChild(leftChild);
            }
        }

        if(leftChild.hasRightChild()) {
            node.setLeftChild(leftChild.getRightChild());
        } else {
            node.setLeftChild(null);
        }

        leftChild.setRightChild(node);

        return leftChild;

    }

    private StringNode rightRightRotation(StringNode previousNode, StringNode node) {

        StringNode rightChild = node.getRightChild();

        if(previousNode == null) {
            root = rightChild;
        } else {
            if(previousNode.hasLeftChild() && previousNode.getLeftChild().getIndex() == node.getIndex()) {
                previousNode.setLeftChild(rightChild);
            } else {
                previousNode.setRightChild(rightChild);
            }
        }

        if(rightChild.hasLeftChild()) {
            node.setRightChild(rightChild.getLeftChild());
        } else {
            node.setRightChild(null);
        }

        rightChild.setLeftChild(node);

        return rightChild;

    }

}