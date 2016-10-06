/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Ítalo Silva
 */
public class CompilationException extends Exception
{

    private int line;
    private int collumn;
    private String errorMessage;
    private String lastToken;
    private int compilationStep;

    CompilationException(int compilationStep, String errorMessage, int line, int collumn, String lastToken)
    {
        this.errorMessage = errorMessage;
        this.collumn = collumn;
        this.line = line;
        this.lastToken = lastToken;
        this.compilationStep = compilationStep;

    }

    @Override
    public String getMessage()
    {
        if (compilationStep == 1)
        {
            return "SCANNER ERROR: " + errorMessage + localization() + lastReadToken();
        }
        else
        {
            if (lastToken.equals("EOF"))
            {
                return "PARSER ERROR: End of file was reached while parsing";
            }
            return "PARSER ERROR: " + errorMessage + localization() + lastReadToken();
        }

    }

    private String localization()
    {
        return " Error found on the line " + line + " and on the collumn " + collumn + ".";
    }

    private String lastReadToken()
    {

        return " Last read token: '" + lastToken + "'.";

    }

}
