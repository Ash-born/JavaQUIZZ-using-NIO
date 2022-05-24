import java.net.*;
import java.io.*;
public class Client {
    public static void main(String[] args) throws Exception {
        try{
            Socket socket=new Socket("127.0.0.1",8888);
            DataInputStream inStream=new DataInputStream(socket.getInputStream());
            DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            String clientMessage="",serverMessage="";

            System.out.print("enter your username : ");
            clientMessage=br.readLine();
            outStream.writeUTF(clientMessage);
            outStream.flush();
            serverMessage=inStream.readUTF();
            System.out.println(serverMessage);

            while(!clientMessage.equals("bye")){

                serverMessage=inStream.readUTF();
                System.out.println(serverMessage);
                if(serverMessage.contains("Question")){
                    System.out.print("ANSWER : ");
                    clientMessage=br.readLine();
                    outStream.writeUTF(clientMessage);
                    outStream.flush();
                }

            }
            outStream.close();
            inStream.close();
            socket.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}