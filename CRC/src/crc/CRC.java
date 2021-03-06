package crc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class CRC {
    private ArrayList<Integer> reminder;
    private ArrayList <Integer> Message = new ArrayList<>();
    private ArrayList <Integer> Transmitted = new ArrayList<>();
	
	                                                       //Setters and getters
    public ArrayList <Integer> getTransmitted() {
        return Transmitted;
    }

    public ArrayList<Integer> getReminder() {
        return reminder;
    }

    public void setReminder(ArrayList<Integer> reminder) {
        this.reminder = reminder;
    }

    public ArrayList <Integer> getMessage() {
        return Message;
    }

    public void setMessage(ArrayList <Integer> Message) {
        this.Message = Message;
    }

    public void setTransmitted(ArrayList <Integer> Transmitted) {
        this.Transmitted = Transmitted;
    }

	                                                        //generator function
	public ArrayList Generator(ArrayList message, ArrayList poly){ 
        CRC GenP=new CRC();
        ArrayList <Integer> toBeTransmitted = new ArrayList<>();
        ArrayList <Integer> Trans = new ArrayList<>();
        
        for(int i=0; i<message.size();i++){
            toBeTransmitted.add((int) message.get(i));
        }
        
        for(int i=0;i<poly.size()-1;i++){
            toBeTransmitted.add(0);
        }
            
        ArrayList <Integer> zeros = new ArrayList<>();
        for(int i=0; i<poly.size();i++){
            zeros.add(0);
        }

        while(toBeTransmitted.size()>=poly.size()){
            if(toBeTransmitted.get(0)==1){
                toBeTransmitted=GenP.Xor(toBeTransmitted, poly);
            }
            else{
                toBeTransmitted=GenP.Xor(toBeTransmitted, zeros);
            }

        } 
                this.setReminder(toBeTransmitted); 
                Trans.addAll(message);
                Trans.addAll(toBeTransmitted);
                this.setTransmitted(Trans);

        return Trans;
    }
    

	                                                          //Verifier function		
															  
    public String verifier ( ArrayList message, ArrayList poly){
        CRC verP=new CRC();
        ArrayList <Integer> remainder = new ArrayList<>();
        String Str = "message is correct";

        remainder=verP.Generator(message, poly);
        
        for(int i=0; i<message.size();i++){
            remainder.remove(0);
        }
               
        for(int i=0; i<remainder.size();i++){
            if(remainder.get(i)==1){
                Str = "message is not correct";
                break;
            }
        }
        return Str;
    } 
    public ArrayList Xor(ArrayList message, ArrayList poly){
        for(int i=0; i<poly.size();i++){
            if(message.get(i)==poly.get(i)){
                message.set(i,0);
            }
            else{
                message.set(i,1);
            }
        }
        message.remove(0);
        return message;
    }





public ArrayList Alter(ArrayList message,int location){
        ArrayList <Integer> alterArray = new ArrayList<>();
        
        for(int i=0; i<message.size();i++){
            int value=(int) message.get(i);
            alterArray.add(value);
        }
        int newLoc =location-1;
                
        for(int i=0; i<alterArray.size();i++){
            if(i==newLoc){
                if(alterArray.get(newLoc)==0){
                    alterArray.set(newLoc, 1);
                }
                else alterArray.set(newLoc, 0);
                }
            }
        return alterArray;
    }
    
    
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
        CRC alterP=new CRC();
        ArrayList <Integer> message = new ArrayList<>();
        ArrayList <Integer> poly = new ArrayList<>();
        String verify;
        
        ArrayList <Integer> transmittedMsg = new ArrayList<>();
        ArrayList <Integer> messageB= new ArrayList<>();


        String messageStr= new String();
        String polyStr= new String();

        try {
                File file = new File("message_generator.txt");
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;

                int counter=0;
                while ((line = bufferedReader.readLine()) != null) {
                        if (counter==0){
                            messageStr=line;
                            counter++;
                        }

                        else if (counter==1){
                        polyStr=line;
                        counter--;
                        }
                }
                fileReader.close();

                for(int i=0; i<messageStr.length();i++){
                    char number= messageStr.charAt(i);
                    int a=Character.getNumericValue(number);
                   message.add(a);
                }

                for(int j=0; j<polyStr.length();j++){
                    char ch= polyStr.charAt(j);
                    int b=Character.getNumericValue(ch);
                    poly.add(b);
                }


        } catch (IOException e) {
                System.out.println("no intput file added");
        }

        transmittedMsg=alterP.Generator(message, poly);
        alterP.setMessage(transmittedMsg);
        String transmittedStr= new String();
        String alterStr= new String();
        
        for(int i=0; i<transmittedMsg.size();i++){ 
            transmittedStr+=transmittedMsg.get(i);
        }     

        try (PrintWriter out = new PrintWriter("transmitted_message.txt")) {
            out.println(transmittedStr);
        }
        
        boolean goOn=true;
        while(goOn){        
            System.out.println("for Alter press 1, for Verifier press 2 ..");

            Scanner console = new Scanner(System.in);
            String toBeDoneFn = console.nextLine();

            if(toBeDoneFn.equals("1")){
                System.out.println("enter The argument of Alter fn:");
                Scanner Bit = new Scanner(System.in);
                String bitToAlterStr = console.nextLine();
                int bitToAlter=Integer.parseInt(bitToAlterStr);
                transmittedMsg=alterP.Alter(transmittedMsg, bitToAlter);

                for(int i=0; i<transmittedMsg.size();i++){ 
                    alterStr+=transmittedMsg.get(i);
                }   
                try (PrintWriter out = new PrintWriter("alterOutput.txt")) {
                    out.println(alterStr);
                }
                verify=alterP.verifier(transmittedMsg, poly);
                try (PrintWriter out = new PrintWriter("VerifierOutput.txt")) {
                    out.println(verify);
                }
                System.out.println("alterOutput.txt and verifier.txt are generated \n");

            }
            
            else if(toBeDoneFn.equals("2")){
                verify=alterP.verifier(transmittedMsg, poly);
                try (PrintWriter out = new PrintWriter("VerifierOutput.txt")) {
                    out.println(verify);
                }
                System.out.println("Verifier.txt is generated \n");
            }
            
            else{
                 System.out.println("no function is available with this number!");
            }
            
            System.out.println("if you want to start all over again press 1, to exit press 2..");
                Scanner opAgain = new Scanner(System.in);
                String opDo = opAgain.nextLine();
                
            if(opDo.equals("1")){
                continue;
            }   
            else{
                goOn=false;
            } 
        }
    }
}