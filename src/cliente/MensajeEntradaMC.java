package cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import org.json.JSONException;
import org.json.JSONObject;
import servidor.Interaccion;

/**
 * Es el hilo encargado de estar siempre pendiente de recibir un mensaje (multicast)
 *
 * @author Karen Castaño Orjuela Castaño
 * @author Carlos Alberto Campos Armero
 */
public class MensajeEntradaMC extends Thread {

    private boolean isAlive;
    private MulticastSocket multicastSocket;
    private Notificable notificable;

    public MensajeEntradaMC(Notificable notificable, MulticastSocket multicastSocket) {
        this.multicastSocket = multicastSocket;
        this.notificable = notificable;
        this.isAlive = true;
    }

    @Override
    public void run() {
        while (isAlive) {

            try {
                DatagramPacket dp = new DatagramPacket(new byte[Interaccion.MAX_BUFFER_SIZE], Interaccion.MAX_BUFFER_SIZE);
                multicastSocket.receive(dp);
                byte[] data = dp.getData();
                JSONObject receiveMessage = new JSONObject(new String(data));

                switch (receiveMessage.getInt("accion")) {
                    case Interaccion.NUEVO_CLIENTE:
                        this.notificable.login(receiveMessage.getString("nombre_usuario") + ": ha iniciado sesión", 1);
                        break;
                    case Interaccion.SESION_NUEVO_CLIENTE:
                        this.notificable.nuevoUsuario(receiveMessage.toString(), 1);
                        break;
                    case Interaccion.NUEVO_MENSAJE:
                        this.notificable.nuevoMensaje(receiveMessage.toString(), 1);
                        break;
                    case Interaccion.SALIDA_CLIENTE:
                        isAlive = false;
                        break;
                    case Interaccion.MOVER_CLIENTE:
                        this.notificable.moverJugador(receiveMessage.toString(), 1);
                        break;
                }
            } catch (IOException ex) {
                System.out.println(">>OCURRIO UN ERROR AL RECIBIR LOS DATOS");
            } catch (JSONException ex) {
                System.out.println(">>OCURRIO UN ERROR AL CONVERTIR LOS DATOS");
            }
        }
    }

}
