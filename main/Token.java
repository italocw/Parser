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
public class Token
{

    private String lexema = null;
    private TokenClassification classification;

    Token(TokenClassification classification)
    {

        this.classification = classification;
    }

    Token(String lexema, TokenClassification classification)
    {
        this.lexema = lexema;
        this.classification = classification;
    }

    public String getLexema()
    {
        return this.lexema;
    }

    public String getClassificationString()
    {
        return classification.name();
    }
    
    public TokenClassification getClassification()
    {
        return classification;
    
    }

  }