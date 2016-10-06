/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ítalo
 */
public class Main
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        String path;
        Scanner scanner;
        File file;
        Token token;
        Parser parser;
        
        if (args.length > 0)
        {
           path = args[0];
            
            try
            {
                file = new File(path);

                parser = new Parser(file);
                parser.parsing();
            }

            catch (FileNotFoundException ex)
            {
                System.out.println("Error when trying to open the file: file not found");
            }
            catch (IOException ex)
            {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else
        {
            System.out.println("You must inform the path the to file where the code is written");
        }
    }

}
