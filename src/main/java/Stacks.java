/**
 * Esta clase representa una pila implementada utilizando una lista enlazada.
 */
public class Stacks {
    /**
     * Clase interna que representa la lista enlazada utilizada para implementar la pila.
     */
    class Stack_LinkedList {
        private ListasEnlazadas.Linkedlist stackList;

        /**
         * Constructor de la clase Stack_LinkedList que inicializa la lista enlazada.
         */
        public Stack_LinkedList() {
            this.stackList = new ListasEnlazadas().new Linkedlist();
        }

        /**
         * Agrega un nuevo elemento a la pila.
         *
         * @param newElement El elemento a agregar a la pila.
         */
        public void push(Object newElement){
            this.stackList.insertFirst(newElement);
        }

        /**
         * Elimina y devuelve el elemento superior de la pila.
         *
         * @return El elemento superior de la pila o null si la pila está vacía.
         */
        public Object pop(){
            ListasEnlazadas.Node node = this.stackList.deleteFirst();
            return (node != null) ? node.getData() : null;
        }
        /**
         * Obtiene el elemento superior de la pila sin eliminarlo.
         *
         * @return El elemento superior de la pila o null si la pila está vacía.
         */
        public Object peek(){
            return this.stackList.getHead().getData();
        }
        /**
         * Verifica si la pila está vacía.
         *
         * @return true si la pila está vacía, false en caso contrario.
         */
        public boolean isEmpty() {
            return this.stackList.isEmpty();
        }
    }
}
