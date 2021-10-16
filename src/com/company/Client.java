package com.company;


import java.io.*;
import java.net.*;

public class Client {
    String nomeServer = "localhost";
    int portaServer = 6789;
    Socket mioSocket;
    BufferedReader tastiera;
    String mieCoordinate;
    String risultatoCoordinate;
    DataOutputStream outVersoServer;
    BufferedReader inDalServer;
    String coordinateGiocatore2;
    String risultatoVersoServer;


    public Socket connetti() {
        System.out.println("2 CLIENT partito in esecuzione ...");

        try {
            // per l'input da tastiera
            tastiera = new BufferedReader(new InputStreamReader(System.in));

            // creo un socket
            mioSocket = new Socket(nomeServer, portaServer);

            // associo due oggetti al socket per effettuare la scrittura e la lettura
            outVersoServer = new DataOutputStream(mioSocket.getOutputStream());
            inDalServer = new BufferedReader(new InputStreamReader(mioSocket.getInputStream()));

        }
        catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la connessione");
            System.exit(1);
        }

        return mioSocket;
    }

    public void comunica() {
        for(;;)
            try {
                // Giocatore 1 inserisce le coordinate e le invia al Giocatore 2
                System.out.println("Inserisci le coordinate : " );
                mieCoordinate = tastiera.readLine();
                outVersoServer.writeBytes(mieCoordinate+"\n");

                // Giocatore 1 riceve il risultato delle coordinate dal Giocatore 2
                risultatoCoordinate = inDalServer.readLine();
                System.out.println("Risultato delle coordinate inviate: "+risultatoCoordinate);

                if(risultatoCoordinate.equals("FINITO")) {
                    System.out.println("CLIENT: termina elaborazione e chiude sessione");
                    mioSocket.close();
                    break;
                }

                //Giocatore 1 riceve le coordinate dal Giocatore 2
                coordinateGiocatore2 = inDalServer.readLine();
                System.out.println("Coordinate del Giocatore 2 : "+coordinateGiocatore2);

                //Giocatore 2 Manda il risultato delle coordinate del Giocatore 1
                System.out.println("Colpito - Non Colpito - Finito: " );
                risultatoVersoServer = tastiera.readLine();
                outVersoServer.writeBytes(risultatoVersoServer+"\n");
                risultatoVersoServer=risultatoVersoServer.toUpperCase();

                if(risultatoVersoServer.equals("FINITO")) {
                    System.out.println("CLIENT: termina elaborazione e chiude sessione");
                    mioSocket.close();
                    break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Errore durante la comunicazione col server!");
                System.exit(1);
            }
    }

    public static void main(String[] args) {
        Client cliente = new Client();
        cliente.connetti();
        cliente.comunica();


    }

}