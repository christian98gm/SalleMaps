package model.list;

public class CustomNode {

    private Object object;
    private CustomNode nextNode;

    public CustomNode() {}

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public CustomNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(CustomNode nextNode) {
        this.nextNode = nextNode;
    }

}