/**
 * Esta clase representa una implementación de listas enlazadas simples.
 */
public class ListasEnlazadas {
    /**
     * Clase interna que representa un nodo en una lista enlazada.
     */
    class Node {
        private Object data;
        private Node next;

        /**
         * Constructor de un nodo con un valor de datos.
         *
         * @param data El valor de datos para este nodo.
         */
        public Node(Object data){
            this.next = null;
            this.data = data;
        }
        /**
         * Obtiene el valor de datos del nodo.
         *
         * @return El valor de datos del nodo.
         */
        public Object getData() {
            return this.data;
        }
        /**
         * Establece el valor de datos del nodo.
         *
         * @param data El nuevo valor de datos para el nodo.
         */
        public void setData(Object data){
            this.data = data;
        }

        /**
         * Obtiene el siguiente nodo en la lista.
         *
         * @return El siguiente nodo en la lista.
         */
        public Node getNext(){
            return this.next;
        }
        /**
         * Establece el siguiente nodo en la lista.
         *
         * @param node El nuevo siguiente nodo.
         */
        public void setNext(Node node){
            this.next = node;
        }
    }
    /**
     * Clase interna que representa una lista enlazada simple.
     */
    class Linkedlist {
        private Node head;
        private int size;

        /**
         * Constructor de la lista enlazada simple.
         */
        public Linkedlist(){
            this.head = null;
            this.size = 0;
        }
        /**
         * Verifica si la lista está vacía.
         *
         * @return true si la lista está vacía, false en caso contrario.
         */
        public boolean isEmpty(){
            return this.head == null;
        }
        /**
         * Obtiene el tamaño de la lista.
         *
         * @return El tamaño de la lista.
         */
        public int size(){
            return this.size;}
        /**
         * Obtiene el nodo cabeza de la lista.
         *
         * @return El nodo cabeza de la lista.
         */
        public Node getHead(){
            return this.head;
        }
        /**
         * Inserta un nuevo dato al principio de la lista.
         *
         * @param data El dato a insertar.
         */
        public void insertFirst(Object data){
            Node newNode = new Node(data);
            newNode.next = this.head;
            this.head = newNode;
            this.size++;
        }
        /**
         * Elimina y devuelve el primer nodo de la lista.
         *
         * @return El primer nodo de la lista o null si la lista está vacía.
         */
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
        /**
         * Muestra los elementos de la lista en consola.
         */
        public void displayList(){
            Node current = this.head;
            while (current != null){
                System.out.println(current.getData());
                current = current.getNext();
            }
        }
        /**
         * Busca un valor en la lista y devuelve el nodo que lo contiene.
         *
         * @param searchValue El valor a buscar en la lista.
         * @return El nodo que contiene el valor o null si no se encuentra.
         */
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
        /**
         * Elimina un valor de la lista y devuelve el nodo que contiene el valor eliminado.
         *
         * @param searchValue El valor a eliminar de la lista.
         * @return El nodo que contiene el valor eliminado o null si el valor no se encuentra en la lista.
         */
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
