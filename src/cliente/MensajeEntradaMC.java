package cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import servidor.Interaccion;

public class MensajeEntradaMC extends Thread {

    private boolean isAlive;

    
    private MulticastSocket multicastSocket;

    public MensajeEntradaMC(MulticastSocket multicastSocket) {
        this.multicastSocket = multicastSocket;
        this.isAlive = true;
    }

    @Override
    public void run() {
        while (isAlive) {

            try {
                DatagramPacket dp = new DatagramPacket(new byte[Interaccion.MAX_BUFFER_SIZE], Interaccion.MAX_BUFFER_SIZE);
                multicastSocket.receive(dp);
                byte[] data = dp.getData();
                
                switch (data[0]) {
                    case Interaccion.NUEVO_CLIENTE:
                        break;
                    case Interaccion.SALIDA_CLIENTE:
                        isAlive = false;
                        break;
                }
                
                System.out.println(new String(data));

            } catch (IOException ex) {
                System.out.println(">>OCURRIO UN ERROR AL RECIBIR LOS DATOS");
            }
        }
    }

}
