

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Objects;

public class Servidor{

    public static void main(String args[]){

        LinkedList<Integer> lista_puertos = new LinkedList<Integer>();

        try {
            ServerSocket server = new ServerSocket(5000);

            while(true){

                /**
                 * Servidor va a continuamente esperar una nueva conneccion, para asi habilitar mas clientes siempre.
                 * Guarda el mensaje que se le envio para procesarlo despues.
                 */
                Socket serversocker =  server.accept();
                DataInputStream datos = new DataInputStream(serversocker.getInputStream());
                String mensajes = datos.readUTF();

                /**
                 * Revisa si el mensaje que le acaba de llegar es un puerto codificado, lo descodifica y lo pone
                 * en una lista enlazada.
                 * Ver Cliente.java
                 */
                if (Objects.equals(String.valueOf(mensajes.charAt(0)), "0")){
                    mensajes = mensajes.substring(1, mensajes.length() );
                    int puerto_final = Integer.parseInt(mensajes);
                    lista_puertos.add(puerto_final);
                    System.out.println("Conectado: " + puerto_final);
                }
                /**
                 * Si el mensaje no es un puerto, es un mensaje normal, entonces itera sobre la lista de
                 * puertos de cliente, crea un socket para cada puerto, y envia el mensaje al socket.
                 */
                else {
                    Socket mensajepuertos = null;

                    for (int i = 0; i < lista_puertos.size(); i++) {
                        mensajepuertos = new Socket("127.0.0.1",lista_puertos.get(i));
                        DataOutputStream out = new DataOutputStream(mensajepuertos.getOutputStream());
                        out.writeUTF(mensajes);
                        mensajepuertos.close();
                    }
                    System.out.println(mensajes);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
