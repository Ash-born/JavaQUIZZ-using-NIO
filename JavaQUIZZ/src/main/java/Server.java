

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import java.net.*;
import java.io.*;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {


    static ArrayList<Socket> players = new ArrayList<>();
    static HashMap<Socket,Integer> player_points = new HashMap<Socket, Integer>();
    static HashMap<Socket,String> player_names= new HashMap<Socket, String>();
    static HashMap<Socket,Integer> player_answers= new HashMap<Socket, Integer>();
    static boolean quizz_started= false;
    static int teste = 0;
    static boolean first_message = true;
    static int correct_answer = 0;

    public static void send_to_all(String message) {
        for (int i = 0; i < players.size(); i++) {
            try {
                DataOutputStream outStream = new DataOutputStream(players.get(i).getOutputStream());
                outStream.writeUTF(message);
                outStream.flush();

            }
            catch (Exception e ){
                System.out.println("COULDN'T SEND MESSAGE TO PLAYER : " +player_names.get(players.get(i)));
            }
        }
    }
    public static void main(String[] args) throws Exception {
        try{
            ServerSocket server=new ServerSocket(8888);

            System.out.println("Server Started ....");

            while(true){

                Socket serverClient=server.accept();  //server accept the client connection request
                threads sct = new threads(serverClient); //send  the request to a separate thread
                sct.start();
                players.add(serverClient);
                player_points.put(serverClient,0);
                test test = new test(serverClient);
                test.start();



            }

        }catch(Exception e){
            System.out.println(e);
        }
    }
}

class test extends  threads{

    test(Socket inSocket) {
        super(inSocket);
    }


    public void run() {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        if (Server.teste == 0) {
            Server.teste =1 ;
            String command = null;
            try {
                command = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (command.equals("begin")) {
                Server.quizz_started = true;
                try {
                    // create Gson instance


                    Gson gson = new Gson();

                    // create a reader
                    Reader reader = Files.newBufferedReader(Paths.get("questions.json"));

                    // convert JSON string to User object
                    JsonElement json = gson.fromJson(reader, JsonElement.class);
                    JsonObject jsonObject = json.getAsJsonObject();
                    JsonArray list = jsonObject.getAsJsonArray("questions");
                    for (int i = 0; i < list.size(); i++) {


                        JsonObject propertiesJson = (JsonObject) list.get(i);
                        String message = "Question " + i + ": " + propertiesJson.get("question").getAsString().replaceAll("\"", " ") + " ? ";
                        JsonArray reponse = propertiesJson.get("answers").getAsJsonArray();
                        String a = reponse.get(0).getAsString();
                        String b = reponse.get(1).getAsString();
                        String c = reponse.get(2).getAsString();
                        String d = reponse.get(3).getAsString();
                        Server.correct_answer = propertiesJson.get("correct_answer").getAsInt();
                        message += " \n A : " + a;
                        message += " \n B : " + b;
                        message += " \n C : " + c;
                        message += " \n D : " + d;

                        System.out.println(message);


                        send_to_all(message);
                        boolean pass = false;
                        while (!pass) {
                            pass = true;
                            for (int j = 0; j < Server.players.size(); j++) {
                                if (!Server.player_answers.containsKey(Server.players.get(j))) {
                                    pass = false;
                                }
                            }

                        }
                        for (int j = 0; j < Server.players.size(); j++) {
                            Server.player_answers.remove(Server.players.get(j));
                            Server.first_message = true;
                        }


                    }
                    String message = "QUIZZ ENDED SHOWING UP RESUlTS : ";
                    for (int j = 0; j < Server.players.size(); j++) {
                        int points = j+1;

                        String test = " \n " + points +  " - " + Server.player_names.get(Server.players.get(j)) + " With  : " + Server.player_points.get(Server.players.get(j)) + " Points .";
                        message += test;
                    }
                    send_to_all(message);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

class threads extends Thread {
    Socket serverClient;
    String client_name;

    threads(Socket inSocket){
        serverClient = inSocket;

    }
    public void run(){
        try{

            DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
            DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
            String clientMessage="", serverMessage="";
            boolean first_client_message = true;
            while(!clientMessage.equals("exit")){
                clientMessage=inStream.readUTF();
                if(first_client_message) {
                    if(!clientMessage.equals("")) {
                      client_name = clientMessage;
                        Server.player_names.put(serverClient,client_name);


                        first_client_message = false;
                        serverMessage = " >> Player " + client_name + " joined the quizz . ";
                        System.out.println(serverMessage);
                        send_to_all(serverMessage);
                        continue;
                    }
                }
                if(Server.quizz_started){
                    if(!Server.player_answers.containsKey(serverClient)) {
                        if(clientMessage.equals("A")) {
                            Server.player_answers.put(serverClient,0);
                            if(Server.correct_answer == 0 ) {
                                if (Server.first_message) {
                                    Server.first_message = false;

                                    int points = Server.player_points.get(serverClient) + 2;
                                    Server.player_points.replace(serverClient, Server.player_points.get(serverClient), points);
                                } else {


                                    int points = Server.player_points.get(serverClient) + 1;
                                    Server.player_points.replace(serverClient, Server.player_points.get(serverClient), points);

                                }
                            }
                        }
                        else if(clientMessage.equals("B")) {
                            Server.player_answers.put(serverClient,1);
                            if(Server.correct_answer == 1 ) {
                                if (Server.first_message) {
                                    Server.first_message = false;

                                    int points = Server.player_points.get(serverClient) + 2;
                                    Server.player_points.replace(serverClient, Server.player_points.get(serverClient), points);
                                } else {


                                    int points = Server.player_points.get(serverClient) + 1;
                                    Server.player_points.replace(serverClient, Server.player_points.get(serverClient), points);

                                }
                            }
                        }
                        else if(clientMessage.equals("C")) {
                            Server.player_answers.put(serverClient,2);
                            if(Server.correct_answer == 2 ) {
                                if (Server.first_message) {
                                    Server.first_message = false;

                                    int points = Server.player_points.get(serverClient) + 2;
                                    Server.player_points.replace(serverClient, Server.player_points.get(serverClient), points);
                                } else {


                                    int points = Server.player_points.get(serverClient) + 1;
                                    Server.player_points.replace(serverClient, Server.player_points.get(serverClient), points);

                                }
                            }
                        }
                        else if(clientMessage.equals("D")) {
                            Server.player_answers.put(serverClient,3);
                            if(Server.correct_answer == 3 ) {
                                if (Server.first_message) {
                                    Server.first_message = false;

                                    int points = Server.player_points.get(serverClient) + 2;
                                    Server.player_points.replace(serverClient, Server.player_points.get(serverClient), points);
                                } else {


                                    int points = Server.player_points.get(serverClient) + 1;
                                    Server.player_points.replace(serverClient, Server.player_points.get(serverClient), points);

                                }
                            }
                        }
                        else {
                            outStream.writeUTF("WRONG ANSWER FORMAT , PLEASE TYPE A or B or C or D to answer");
                            outStream.flush();
                        }
                    }
                }

            }
            inStream.close();
            outStream.close();
            serverClient.close();
            Server.players.remove(serverClient);
        }catch(Exception ex){
            System.out.println(ex);
        }finally{
            System.out.println(" >> Player " + client_name + " exited the quizz !! ");
        }
    }
    public void send_to_all(String message) {
        for (int i = 0; i < Server.players.size(); i++) {
            try {
                DataOutputStream outStream = new DataOutputStream(Server.players.get(i).getOutputStream());
                outStream.writeUTF(message);
                outStream.flush();

            }
            catch (Exception e ){
                System.out.println("COULDN'T SEND MESSAGE TO PLAYER : " + Server.player_names.get(Server.players.get(i)));
            }
        }
    }
}

