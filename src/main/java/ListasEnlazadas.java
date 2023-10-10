public class ListasEnlazadas {

    class Node {
        private Object data;
        private Node next;

        public Node(Object data){
            this.next = null;
            this.data = data;
        }
        public Object getData() {
            return this.data;
        }
        public void setData(Object data){
            this.data = data;
        }
        public Node getNext(){
            return this.next;
        }
        public void setNext(Node node){
            this.next = node;
        }
    }

    class Linkedlist {
        private Node head;
        private int size;

        public Linkedlist(){
            this.head = null;
            this.size = 0;
        }
        public boolean isEmpty(){
            return this.head == null;
        }
        public int size(){
            return this.size;}
        public Node getHead(){
            return this.head;
        }
        public void insertFirst(Object data){
            Node newNode = new Node(data);
            newNode.next = this.head;
            this.head = newNode;
            this.size++;
        }
        public Node deleteFirst(){
            if (this.head != null){
                Node temp = this.head;
                this.head = this.head.next;
                this.size--;
                return temp;
            }else{
                return null;
            }
        }
        public void displayList(){
            Node current = this.head;
            while (current != null){
                System.out.println(current.getData());
                current = current.getNext();
            }
        }
        public Node find(Object searchValue) {
            Node current = this.head;
            while (current != null){
                if (current.getData().equals(searchValue)){
                    return current;
                } else {
                    current = current.getNext();
                }
            }
            return null;
        }
        public Node delete(Object searchValue){
            Node current = this.head;
            Node previous = this.head;

            while(current != null){
                if (current.getData().equals(searchValue)){
                    if (current == this.head){
                        this.head = this.head.getNext();
                    } else {
                        previous.setNext(current.getNext());
                    }
                    return current;
                } else {
                    previous = current;
                    current = current.getNext();
                }
            }
            return null;
        }
    }
}
