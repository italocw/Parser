/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Ítalo
 */
public class File
{

    private int collumn, line, previousLineCollumn;
    private char characterAfterLine;
    private FileReader code;

    File(String path) throws FileNotFoundException
    {
        code = new FileReader(path);
        this.line = 0;
        this.collumn = 0;
       
    }
    public int getNextData() throws IOException
    {
        
        return code.read();
    }
         
    public boolean isThereCharacterAfterLine() throws IOException
    {
        int characterBeforeCasting = code.read();
        char character = (char) characterBeforeCasting;
        while ( character != '\r'  && character != '\n'  && characterBeforeCasting != -1)
        {
            characterBeforeCasting = code.read();
        character = (char) characterBeforeCasting;
        
        
        }  
       if (characterBeforeCasting == -1)
       {
           return false;
       }
       else
       {
           characterAfterLine = character;
           collumn =0;
           incrementCollumn (characterAfterLine);
           line++;
           return true;
       }
    }

    public void incrementCollumn(char character)
    {
        if (character == '\t')
        {
            collumn = collumn + 4;
        }
        else
        {
            collumn++;
        }
    }
    public char getCharacterAfterLine()
    {
        return characterAfterLine;
    }
    
    public void incrementLine()
    {
	line++;
    }

    public void resetCollumn(char character)
    {
	if(character == '\n' || character == '\r')
	{
		previousLineCollumn = collumn;
	}
        collumn = 1;	
    }

    public int getCollumn(char character)
    {
         if(character == '\n' || character == '\r')
	{
		return previousLineCollumn;
	}
        
	else
	{
		 return collumn;
    	}   
    }

    public int getLine(char character)
    {
  	if(character == '\n' || character == '\r')
	{
		return line - 1;
	}
        
	else
	{	        
     	  return line;
        }
    }

}
