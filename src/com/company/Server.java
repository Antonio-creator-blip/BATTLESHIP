package com.company;



import java.io.*;
import java.net.*;
import java.util.*;





public class Server {

    ServerSocket server = null;
    Socket client = null;
    String coordinateGiocatore1;
    String mieCoordinate;
    String risultatoVersoClient;
    BufferedReader inDalClient;
    BufferedReader tastiera2;
    DataOutputStream outVersoClient;
    String risultatoCoordinate;

    public Socket attendi() {
        try {
            System.out.println("1 SERVER partito in esecuzione ...");

            tastiera2 = new BufferedReader(new InputStreamReader(System.in));

            // creo un server sulla porta 6789
            server = new ServerSocket(6789);

            // rimane in attesa di un client
            client = server.accept();

            //chiuso il server per inibire altri client
            server.close();

            // associo due oggetti al socket del client per effettuare la scrittura e la lettura
            inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outVersoClient = new DataOutputStream(client.getOutputStream());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante l'istanza del server! ");
            System.exit(1);
        }
        return client;
    }

    public void comunica() {
        try {
            while(true) {
                System.out.println("Connesso");

                //Giocatore 2 riceve le coordinate dal Giocatore 1
                coordinateGiocatore1 = inDalClient.readLine();
                System.out.println("Coordinate del Giocatore 1 : "+coordinateGiocatore1);

                //Giocatore 2 Manda il risultato delle coordinate del Giocatore 1
                System.out.println("Colpito - Non Colpito - Finito: " );
                risultatoVersoClient = tastiera2.readLine();
                outVersoClient.writeBytes(risultatoVersoClient+"\n");
                risultatoVersoClient=risultatoVersoClient.toUpperCase();

                if(risultatoVersoClient.equals("FINITO")) {
                    System.out.println("SERVER: termina elaborazione e chiude sessione");
                    client.close();
                    break;
                }

                //Giocatore2 inserisce le sue coordinate e le invia al Giocatore 1
                System.out.println("Inserisci le coordinate : " );
                mieCoordinate = tastiera2.readLine();
                outVersoClient.writeBytes(mieCoordinate+"\n");

                // Giocatore 1 riceve il risultato delle coordinate dal Giocatore 2
                risultatoCoordinate = inDalClient.readLine();
                System.out.println("Risultato delle coordinate inviate: "+risultatoCoordinate);
                risultatoCoordinate=risultatoCoordinate.toUpperCase();

                if(risultatoCoordinate.equals("FINITO")) {
                    System.out.println("SERVER: termina elaborazione e chiude sessione");
                    client.close();
                    break;
                }

            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    public static void main(String args[]) {
        Server servente = new Server();
        servente.attendi();
        servente.comunica();
    }

}
