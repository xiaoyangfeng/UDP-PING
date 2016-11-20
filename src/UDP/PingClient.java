/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;

/**
 *
 * @author Blue
 */
import java.io.*;
import java.net.*;
import java.util.*;

/*
 * Server to process ping requests over UDP.
 */
public class PingClient {

    private static final int DELAY = 1000;  // milliseconds

    public static void main(String[] args) throws Exception {
        // Get command line argument.
        if (args.length != 2) {
            System.out.println("Required arguments: Server port");
            return;
        }
        InetAddress server = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);

        // Create random number generator for use in simulating 
        // packet loss and network delay.
        //Random random = new Random();
        // Create a datagram socket for receiving and sending UDP packets
        // through the port specified on the command line.
        DatagramSocket socket = new DatagramSocket(port+1);

        // Processing loop.
        int number = 0;
        while (number < 10) {
            Date data = new Date();
            long sendTime = data.getTime();
            String mess = "PING " + number + " " + sendTime + " \n";
            byte[] sendByte = new byte[1024];
            sendByte = mess.getBytes();
            // Create a datagram packet to hold incomming UDP packet.
            DatagramPacket request = new DatagramPacket(sendByte, sendByte.length, server, port);
            socket.send(request);
            // Block until the host receives a UDP packet.
            try{
            socket.setSoTimeout(DELAY);
            DatagramPacket response = new DatagramPacket(new byte[1024], 1024);                 
            socket.receive(response);
            //System.out.println(number);
            printData(response);
            }
            catch(IOException e){
                System.out.println("Timeout: #" + number);
            }
            number++;

        }
    }

    /* 
    * Print ping data to the standard output stream.
     */
    private static void printData(DatagramPacket request) throws Exception {
        // Obtain references to the packet's array of bytes.
        byte[] buf = request.getData();
        
        // Wrap the bytes in a byte array input stream,
        // so that you can read the data as a stream of bytes.
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);

        // Wrap the byte array output stream in an input stream reader,
        // so you can read the data as a stream of characters.
        InputStreamReader isr = new InputStreamReader(bais);

        // Wrap the input stream reader in a bufferred reader,
        // so you can read the character data a line at a time.
        // (A line is a sequence of chars terminated by any combination of \r and \n.) 
        BufferedReader br = new BufferedReader(isr);

        // The message data is contained in a single line, so read this line.
        String line = br.readLine();

        // Print host address and data received from it.
        System.out.println(
                "Received from "
                + request.getAddress().getHostAddress()
                + ": "
                + new String(line));
    }
}
