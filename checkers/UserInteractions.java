/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;
import java.io.*;
import java.util.Vector;

/**
 *
 * @author ASHISH
 */
public class UserInteractions {        
    
    public static String GameChoice()
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        // Ask for user input
        PrintSeparator('#');
        System.out.println("Welcome To The Checkers Game developed by:");
        System.out.println(" \t1. Ashish Prasad ashishp@iitrpr.ac.in");
        System.out.println(" \t2. Apurv Verma   apurvverma@iitrpr.ac.in");
        PrintSeparator('-');
        System.out.println("Enter 'w/W' if you want to play as white.");
        System.out.println("Enter 'b/B' if you want to play as black.");
        System.out.println("Enter 'a/A' for two player game.");
        System.out.println("Enter 'n/N' for auto play.");
        PrintSeparator('#');
        
        String choice = new String();
                
        while (true)
        {
            try {
                System.out.print("Enter your Choice (w/W/b/B/a/A/n/N): ");
                choice = br.readLine().toLowerCase();
                
                if (choice.equals("w")||choice.equals("b")||choice.equals("a")||choice.equals("n")){
                    break;                
                }
            } catch (Exception ex) {}
            
            System.out.println("\nWrong Choice...Type again(0-7): ");
        }
        
        return choice;        
    }
    
    public static Move getNextMove(Player player){
        return TakeUserInput(-1,-1);
    }
    
    // Pass r1 to be -1 and c1 to be -1 if we want to take r1 and c1 as an input from user
    public static Move TakeUserInput(int r1, int c1){        
        // Display the game board        
        Game.board.Display();
        PrintSeparator('-'); 
        
        // Ask for user input
        System.out.println("Enter your Move.");
        System.out.println("Piece To Move:"); 
        
        System.out.print("\tRow(0-7): ");
        if (r1==-1){            
            r1 = TakeInput();            
        }
        else{
            System.out.println(r1);
        }
        
        System.out.print("\tCol(0-7): ");
        if (c1==-1){            
            c1 = TakeInput();
        }
        else{
            System.out.println(c1);
        }
        
                
        System.out.println("Where To Move:");
        System.out.print("\tRow(0-7): ");                
        int r2 = TakeInput();
        
        System.out.print("\tCol(0-7): ");
        int c2 = TakeInput();
        
        return new Move(r1,c1,r2,c2);
    }
    
    private static int TakeInput(){
        
        int num = -1;        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        while (true)
        {
            try {
                num = Integer.parseInt(br.readLine());
                
                if (num>=0 && num < Board.rows){
                    break;                
                }
            } catch (Exception ex) {}
            
            System.out.print("Wrong Choice...Type again(0-7): ");
        }
        
        return num;
    }
    
    public static void PrintSeparator(char ch)
    {
        switch(ch){
            case '_':
                System.out.println("___________________________________________________________________________");
                break;
            case '-':
                System.out.println("---------------------------------------------------------------------------");
                break;
            case '#':
                System.out.println("###########################################################################");
                break;            
        }
    }
    
    public static void DisplayGreetings(Player color) {
        
        Game.board.Display();
        PrintSeparator('_');
        
        if (color.equals(Player.white)){
            System.out.println("Congrats!!!!!!!!!! White has Won.");
        }
        else{
            System.out.println("Congrats!!!!!!!!!! Black has Won.");
        }
    }
        
    public static void DisplayMoveSeq(Vector<Move> moveSeq){
        for(Move m:moveSeq){
            m.display();
            System.out.print(", ");
        }
 
        System.out.println();
    }
}