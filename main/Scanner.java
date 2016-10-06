/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;

/**
 *
 * @author Ítalo 
 */

public class Scanner
{

    private char lookAhead;
    private String newToken;
    private File file;
    private int multipleLinesCommentInicialLine;
    private int multipleLinesCommentInicialCollumn;
    private int lookAheadBeforeCasting;
    

    Scanner(File file) throws IOException
    {

        lookAheadBeforeCasting = file.getNextData();
        lookAhead = (char) lookAheadBeforeCasting;
        this.file = file;
        file.resetCollumn(lookAhead);
        file.incrementLine();
        findNextLookAheadAfterBlank();

    }

    private boolean isBlank(char character)
    {
        return (character == ' ' || character == '\t' || character == '\n' || character == '\r');
    }

    public void findNextLookAheadAfterBlank() throws IOException
    {
        while (isBlank(lookAhead) && lookAheadBeforeCasting != -1)
        {
            lookAheadBeforeCasting = file.getNextData();
            lookAhead = (char) lookAheadBeforeCasting;
            if (lookAhead == '\n' || lookAhead == '\r')
            {
                file.resetCollumn(lookAhead);
                file.incrementLine();
            }
            else
            {
                file.incrementCollumn(lookAhead);
            }            
          
        }
    }

  
    private void updateLookAhead() throws IOException
    {
        lookAheadBeforeCasting =  file.getNextData();

        lookAhead = (char) lookAheadBeforeCasting;
       
        if ( lookAhead == '\n' || lookAhead == '\r')
        {
            file.resetCollumn(lookAhead);
            file.incrementLine();
        }
         else
        {
            file.incrementCollumn(lookAhead);
        }
    }

    private boolean isAlphabet(char character)
    {
        return ((character >= 'a' && character <= 'z') || character >= 'A' && character <= 'Z');
    }

    private boolean isDigit(char character)
    {
        return character >= '0' && character <= '9';
    }

    public Token getNextToken() throws IOException, CompilationException
    {

        newToken = "";

        findNextLookAheadAfterBlank();

        if (lookAheadBeforeCasting == -1)
        {
            return new Token("EOF", TokenClassification.EOF_CHARACTER);
        }
        else if (isAlphabet(lookAhead) || lookAhead == '_')
        {
            return verifyAlphabetOrUnderline();
        }
        else if (isDigit(lookAhead))
        {
            return verifyDigit();
        }
        else if (lookAhead == '.')
        {
            return verifyFloat();
        }
        else if (lookAhead == 39)
        {
            return verifyChar();
        }
        else if (lookAhead == '(')
        {
         
            updateLookAhead();
            findNextLookAheadAfterBlank();
            
            return new Token("(", TokenClassification.LEFT_PARENTHESIS);
        }
        else if (lookAhead == ')')
        {
            updateLookAhead();
             findNextLookAheadAfterBlank();
            return new Token(")",TokenClassification.RIGHT_PARENTHESIS);
        }
        else if (lookAhead == '{')
        {
            updateLookAhead();
             findNextLookAheadAfterBlank();
            return new Token("{", TokenClassification.LEFT_CURLY_BRACKET);
        }
        else if (lookAhead == '}')
        {
            updateLookAhead();
             findNextLookAheadAfterBlank();
            return new Token("}", TokenClassification.RIGHT_CURLY_BRACKET);
        }
        else if (lookAhead == ';')
        {
            updateLookAhead();
             findNextLookAheadAfterBlank();
            return new Token(";", TokenClassification.SEMICOLON);
        }
        else if (lookAhead == ',')
        {
            updateLookAhead();
             findNextLookAheadAfterBlank();
            return new Token(",", TokenClassification.COMMA);
        }
        else if (lookAhead == '=')
        {
            return verifyEqualSign();
        }
        else if (lookAhead == '>')
        {
            return verifyGreaterThanSign();
        }
        else if (lookAhead == '<')
        {
            return verifyLessThanSign();
        }
        else if (lookAhead == '!')
        {
            return verifyDifferentFromSign();
        }
        else if (lookAhead == '+')
        {
            updateLookAhead();
             findNextLookAheadAfterBlank();
            return new Token("+",TokenClassification.SUM_OP);
        }
        else if (lookAhead == '-')
        {
            updateLookAhead();
             findNextLookAheadAfterBlank();
            return new Token("-",TokenClassification.SUBTRACTION_OP);
        }
        else if (lookAhead == '*')
        {
            updateLookAhead();
             findNextLookAheadAfterBlank();
            return new Token("*", TokenClassification.MULTIPLICATION_OP);
        }
        else if (lookAhead == '/')
        {
            return verifyBar();
        }
       
        else
        {
            throw new CompilationException(1,"Invalid character.", file.getLine(lookAhead), file.getCollumn(lookAhead) -1, newToken);
        }
       
        
    }

    private Token verifyMultipleLinesComment() throws IOException, CompilationException
    {

        while (lookAhead != '*')
        {
            if (lookAheadBeforeCasting == -1)
            {

                throw new CompilationException(1, "Multiple lines comment was not closed. The following localization shows where it was opened ->", multipleLinesCommentInicialLine, multipleLinesCommentInicialCollumn, "/*");
            }
            updateLookAhead();
        }
        updateLookAhead();

        if (lookAheadBeforeCasting == -1)
        {
            
            throw new CompilationException(1, "Multiple lines comment was not closed. The following localization shows where it was opened ->", multipleLinesCommentInicialLine, multipleLinesCommentInicialCollumn, "/*");
        }

        if (lookAhead == '/')
        {
            updateLookAhead();
             findNextLookAheadAfterBlank();
            return new Token("/* */", TokenClassification.MULTIPLE_LINES_COMMENT);
        }
        else
        {
            return verifyMultipleLinesComment();
        }
    }

    private Token verifyBar() throws IOException, CompilationException
    {
        newToken = newToken + lookAhead;
        updateLookAhead();
        switch (lookAhead)
        {
            case '/':
                if (file.isThereCharacterAfterLine())
                {
                    lookAhead = file.getCharacterAfterLine();
                     findNextLookAheadAfterBlank();
                }
                else
                {
                    lookAheadBeforeCasting = -1;
                }
               
                
                return new Token("//",TokenClassification.ONE_LINE_COMMENT);
            case '*':

                multipleLinesCommentInicialLine = file.getLine(lookAhead);
                multipleLinesCommentInicialCollumn = file.getCollumn(lookAhead);
                return verifyMultipleLinesComment();
            default:
                
                return new Token("/", TokenClassification.DIVISION_OP);
        }
    }

    private Token verifyDifferentFromSign() throws IOException, CompilationException
    {

        updateLookAhead();
        if (lookAhead == '=')
        {
             updateLookAhead();
             findNextLookAheadAfterBlank();
            return new Token("!=", TokenClassification.DIFERENT_FROM_COMPARATOR);
        }
        else 
        {
            throw new CompilationException(1, "'!' must be followed by '='. " + treatIfCharacterIsNotDisplayed(lookAhead) + " found. ", file.getLine(lookAhead), file.getCollumn(lookAhead) -1, newToken);
        }
    }

    private Token verifyGreaterThanSign() throws IOException
    {

        newToken = newToken + lookAhead;
        updateLookAhead();
        if (lookAhead == '=')
        {
            newToken = newToken + lookAhead;
            updateLookAhead();
             findNextLookAheadAfterBlank();
            return new Token(">=", TokenClassification.GREATER_THAN_OR_EQUAL_TO_COMPARATOR);
        }
        else
        {
            return new Token(">", TokenClassification.GREATER_THAN_COMPARATOR);
        }
    }

    private Token verifyLessThanSign() throws IOException
    {
        updateLookAhead();
        if (lookAhead == '=')
        {
            updateLookAhead();
             findNextLookAheadAfterBlank();
            return new Token("<=", TokenClassification.LESS_THAN_OR_EQUAL_TO_COMPARATOR);
        }
        else
        {
             findNextLookAheadAfterBlank();
            return new Token("<", TokenClassification.LESS_THAN_COMPARATOR);
        }
    }

    private Token verifyEqualSign() throws IOException
    {
        updateLookAhead();
        if (lookAhead == '=')
        {
            updateLookAhead();
             findNextLookAheadAfterBlank();
            return new Token("==", TokenClassification.EQUAL_TO_COMPARATOR);
        }
        else
        {
             findNextLookAheadAfterBlank();
            return new Token("=", TokenClassification.ATRIBUITION_SIGN);
        }
    }

    private Token verifyAlphabetOrUnderline() throws IOException
    {
        newToken = newToken + lookAhead;
        updateLookAhead();

        while (isDigit(lookAhead) || isAlphabet(lookAhead) || lookAhead == '_')
        {
            newToken = newToken + lookAhead;
            updateLookAhead();

        }

       return verifyReservedWord(newToken);
       
    }

    private Token verifyReservedWord(String word) throws IOException
    {
         findNextLookAheadAfterBlank();
        switch (word)
        {
            case "main":
                return new Token("main", TokenClassification.RESERVED_WORD_MAIN);
            case "if":
                return new Token("if", TokenClassification.RESERVED_WORD_IF);
            case "else":
                return new Token("else", TokenClassification.RESERVED_WORD_ELSE);
            case "while":
                return new Token("while", TokenClassification.RESERVED_WORD_WHILE);
            case "do":
                return new Token("do", TokenClassification.RESERVED_WORD_DO);
            case "for":
                return new Token("for", TokenClassification.RESERVED_WORD_FOR);
            case "int":
                return new Token("int", TokenClassification.RESERVED_WORD_INT);
            case "float":
                return new Token("float", TokenClassification.RESERVED_WORD_FLOAT);
            case "char":
                return new Token("char", TokenClassification.RESERVED_WORD_CHAR);
            
            default:  return new Token(newToken, TokenClassification.IDENTIFIER);
        }
    }

    private Token verifyChar() throws IOException, CompilationException
    {
        newToken = newToken + lookAhead;
        updateLookAhead();

        if (isDigit(lookAhead) || isAlphabet(lookAhead))
        {
            newToken = newToken + lookAhead;
            updateLookAhead();
            if (lookAhead == 39)
            {
                newToken = newToken + lookAhead;
                updateLookAhead();
                 findNextLookAheadAfterBlank();
                return new Token(newToken, TokenClassification.CHARACTER);
            }
            else
            {

                throw new CompilationException(1, "Not well-formed char. ''' expected, '" + treatIfCharacterIsNotDisplayed(lookAhead) + "' found. ", file.getLine(lookAhead), file.getCollumn(lookAhead) -1, newToken);

            }
        }
        else
        {
            throw new CompilationException(1, "Not well-formed char. Alphanumeric character expected, '" + treatIfCharacterIsNotDisplayed(lookAhead) + "' found. ", file.getLine(lookAhead), file.getCollumn(lookAhead) -1, newToken);
        }
    }

    private Token verifyDigit() throws IOException, CompilationException
    {
        newToken = newToken + lookAhead;
        updateLookAhead();

        while (isDigit(lookAhead))
        {
            newToken = newToken + lookAhead;
            updateLookAhead();
        }
        if (lookAhead == '.')
        {
            return verifyFloat();
        }
        else
        {
             findNextLookAheadAfterBlank();
            return new Token(newToken, TokenClassification.INTEGER_VALUE);
        }
    }

    private Token verifyFloat() throws IOException, CompilationException
    {
        newToken = newToken + lookAhead;
        updateLookAhead();
        if (isDigit(lookAhead))
        {
            while (isDigit(lookAhead))
            {
                newToken = newToken + lookAhead;
                updateLookAhead();
            }
             findNextLookAheadAfterBlank();
            return new Token(newToken, TokenClassification.FLOAT_VALUE);
        }
        else
        {
            throw new CompilationException(1, "Not well-formed float. DIGIT expected, '" + treatIfCharacterIsNotDisplayed(lookAhead) + "' found. ", file.getLine(lookAhead), file.getCollumn(lookAhead) -1, newToken);
        }
    }

    private char treatIfCharacterIsNotDisplayed(char character)
    {
        if (character == '\n' || character == '\r')
        {
            return ' ';
        }
        else
        {
            return character;
        }
    }
}
