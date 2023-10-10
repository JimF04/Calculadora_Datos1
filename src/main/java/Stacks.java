public class Stacks {
    class Stack_LinkedList {
        private ListasEnlazadas.Linkedlist stackList;

        public Stack_LinkedList() {
            this.stackList = new ListasEnlazadas().new Linkedlist();
        }

        public void push(Object newElement){
            this.stackList.insertFirst(newElement);
        }
        public Object pop(){
            ListasEnlazadas.Node node = this.stackList.deleteFirst();
            return (node != null) ? node.getData() : null;
        }
        public Object peek(){
            return this.stackList.getHead().getData();
        }

        public boolean isEmpty() {
            return this.stackList.isEmpty();
        }
    }
}
