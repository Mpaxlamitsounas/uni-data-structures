package unipi.dataStructures.exer1;

class Node {

	public Arc arc;
    public Node next;
    public Node prev;

	public Node(Arc arc) {
		this.arc = arc;
	}

    public void linkNext(Node next) {
        this.next = next;
    }

    public void linkPrev(Node prev) {
        this.prev = prev;
    } 

    public Node copy() {
        return new Node(arc);
    }

}