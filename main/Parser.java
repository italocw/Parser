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
 * @author italo
 */
public class Parser
{

    private File file;
    private Scanner scanner;
    private Token lookAhead;
    private int lineBeforeLastToken;
    private int collumnBeforeLastToken;

    private TokenClassification typeFirst[] =
    {
        TokenClassification.RESERVED_WORD_CHAR,
        TokenClassification.RESERVED_WORD_FLOAT,
        TokenClassification.RESERVED_WORD_INT,

    };
    
    private TokenClassification iterationFirst[] =
    {
        TokenClassification.RESERVED_WORD_WHILE,
        TokenClassification.RESERVED_WORD_DO
    };

    public Parser(File file) throws IOException
    {
        scanner = new Scanner(file);
        lineBeforeLastToken = 1;
        collumnBeforeLastToken = 1;

        this.file = file;

    }

    public void parsing() throws IOException
    {

        try
        {
            lookAhead = scanner.getNextToken();

            if (TokenClassification.EOF_CHARACTER == lookAhead.getClassification())
            {
                throw new CompilationException(2, "File cannot be empty. ", lineBeforeLastToken, collumnBeforeLastToken, "EOF Character");

            }
            else
            {
                program();

            }

            if (lookAhead.getClassificationString().equals("EOF_CHARACTER") == false)
            {
                throw new CompilationException(2, "Part of the code is written after the end of the main block. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

            }

        }

        catch (CompilationException ex)
        {

            System.out.println(ex.getMessage());
        }
    }

    private void program() throws IOException, CompilationException
    {
        if (TokenClassification.RESERVED_WORD_INT == lookAhead.getClassification())
        {
            updateLookAhead();

            if (TokenClassification.RESERVED_WORD_MAIN == lookAhead.getClassification())
            {
                updateLookAhead();

                if (TokenClassification.LEFT_PARENTHESIS == lookAhead.getClassification())
                {
                    updateLookAhead();

                    if (TokenClassification.RIGHT_PARENTHESIS == lookAhead.getClassification())
                    {
                        updateLookAhead();
                        block();

                    }

                    else
                    {
                        throw new CompilationException(2, "')' expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

                    }
                }
                else
                {
                    throw new CompilationException(2, "'(' expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

                }
            }
            else
            {
                throw new CompilationException(2, "Reserved word 'main' expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

            }
        }
        else
        {
            throw new CompilationException(2, "Reserved word 'int' expected, but token '" + lookAhead.getLexema() + "' was found, ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

        }

    }

    private void block() throws IOException, CompilationException
    {

        if (TokenClassification.LEFT_CURLY_BRACKET == lookAhead.getClassification())
        {
            updateLookAhead();
            while (isType(lookAhead))
            {
                updateLookAhead();
                declaration();

            }
            while (verifyCommandPossibility(lookAhead))
            {
                command();

            }
            if (TokenClassification.RIGHT_CURLY_BRACKET == lookAhead.getClassification())
            {
                updateLookAhead();
            }
            else if (TokenClassification.RESERVED_WORD_ELSE == lookAhead.getClassification())
            {
                throw new CompilationException(2, "'else' without 'if'.", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

            }
            else
            {
                throw new CompilationException(2, "COMMAND or '}' expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());
            }

        }
        else
        {
            throw new CompilationException(2, "{' expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

        }
    }

    private void updateLookAhead() throws CompilationException, IOException
    {
        lineBeforeLastToken = file.getLine(' ');
        collumnBeforeLastToken = file.getCollumn(' ') - 1;
        lookAhead = scanner.getNextToken();
        while (lookAhead.getClassification() == TokenClassification.MULTIPLE_LINES_COMMENT || lookAhead.getClassification() == TokenClassification.ONE_LINE_COMMENT)
        {
            lookAhead = scanner.getNextToken();
        }

    }

    private boolean isType(Token token)
    {
        for (TokenClassification t : typeFirst)
        {
            if (t == token.getClassification())
            {
                return true;
            }
        }
        return false;

    }

    private void declaration() throws IOException, CompilationException
    {

        if (lookAhead.getClassification() == TokenClassification.IDENTIFIER)
        {
            updateLookAhead();

            while (lookAhead.getClassification() == TokenClassification.COMMA)
            {
                updateLookAhead();

                if (lookAhead.getClassification() == TokenClassification.IDENTIFIER)
                {

                    updateLookAhead();
                }
                else
                {
                    throw new CompilationException(2, "IDENTIFIER expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

                }
            }
            if (lookAhead.getClassification() == TokenClassification.SEMICOLON)
            {
                updateLookAhead();
            }
            else
            {
                throw new CompilationException(2, "';' or ',' expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

            }
        }
        else
        {
            throw new CompilationException(2, "IDENTIFIER expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

        }
    }

    private boolean verifyCommandPossibility(Token lookAhead)
    {
        if (lookAhead.getClassification() == TokenClassification.IDENTIFIER || lookAhead.
                getClassification() == TokenClassification.LEFT_CURLY_BRACKET || lookAhead.
                getClassification() == TokenClassification.RESERVED_WORD_WHILE || lookAhead.
                getClassification() == TokenClassification.RESERVED_WORD_DO || lookAhead.
                getClassification() == TokenClassification.RESERVED_WORD_IF)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean verifyIterationPossibility(Token token)
    {
        return TokenClassification.RESERVED_WORD_DO == token.getClassification() || TokenClassification.RESERVED_WORD_WHILE == token.getClassification();

    }

    private void command() throws CompilationException, IOException
    {
        if (lookAhead.getClassification() == TokenClassification.IDENTIFIER)
        {
            attribution();
        }
        else if (lookAhead.getClassification() == TokenClassification.LEFT_CURLY_BRACKET)
        {
            block();
        }
        else if (verifyIterationPossibility(lookAhead))
        {
            iteration();
        }

        else if (lookAhead.getClassification() == TokenClassification.RESERVED_WORD_IF)
        {
            updateLookAhead();

            if (lookAhead.getClassification() == TokenClassification.LEFT_PARENTHESIS)
            {
                updateLookAhead();
                relacionalExpression();

                if (lookAhead.getClassification() == TokenClassification.RIGHT_PARENTHESIS)
                {
                    updateLookAhead();
                    command();

                    if (lookAhead.getClassification() == TokenClassification.RESERVED_WORD_ELSE)
                    {
                        updateLookAhead();
                        command();
                    }

                }
                else
                {
                    throw new CompilationException(2, "')' expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

                }
            }
            else
            {
                throw new CompilationException(2, "'(' expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

            }
        }
        else
        {
            throw new CompilationException(2, "COMMAND expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

        }

    }

    private void iteration() throws IOException, CompilationException
    {
        if (TokenClassification.RESERVED_WORD_WHILE == lookAhead.getClassification())
        {
            updateLookAhead();
            if (TokenClassification.LEFT_PARENTHESIS == lookAhead.getClassification())
            {
                updateLookAhead();
                relacionalExpression();

                if (TokenClassification.RIGHT_PARENTHESIS == lookAhead.getClassification())
                {
                    updateLookAhead();
                    command();

                }
                else
                {
                    throw new CompilationException(2, "')' expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());
                }

            }
            else
            {
                throw new CompilationException(2, "'(' expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

            }
        }
        else if (TokenClassification.RESERVED_WORD_DO == lookAhead.getClassification())
        {
            updateLookAhead();
            command();
            if (TokenClassification.RESERVED_WORD_WHILE == lookAhead.getClassification())
            {
                updateLookAhead();
                if (TokenClassification.LEFT_PARENTHESIS == lookAhead.getClassification())
                {
                    updateLookAhead();
                    relacionalExpression();

                    if (TokenClassification.RIGHT_PARENTHESIS == lookAhead.getClassification())
                    {
                        updateLookAhead();
                        if (TokenClassification.SEMICOLON == lookAhead.getClassification())
                        {
                            updateLookAhead();
                        }
                        else
                        {
                            throw new CompilationException(2, "';' expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

                        }
                    }
                    else
                    {
                        throw new CompilationException(2, "')' or ',' expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

                    }

                }
                else
                {
                    throw new CompilationException(2, "'(' expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

                }
            }
            else
            {
                throw new CompilationException(2, "'while' expected, but '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

            }
        }
        else
        {
            throw new CompilationException(2, "'while' or 'do' expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

        }
    }

    private void relacionalExpression() throws IOException, CompilationException
    {
        arithmeticExpression();
        if (isRelacionalOp(lookAhead))
        {
            updateLookAhead();
            arithmeticExpression();
        }
        else
        {
            throw new CompilationException(2, "Relacional operator expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

        }
    }

    private void attribution() throws IOException, CompilationException
    {
        if (TokenClassification.IDENTIFIER == lookAhead.getClassification())
        {
            updateLookAhead();
            if (TokenClassification.ATRIBUITION_SIGN == lookAhead.getClassification())
            {
                updateLookAhead();
                arithmeticExpression();
                if (TokenClassification.SEMICOLON == lookAhead.getClassification())
                {
                    updateLookAhead();
                }
                else
                {
                    throw new CompilationException(2, "';' or arithmetic operator expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

                }

            }
            else
            {
                throw new CompilationException(2, "'=' expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());
            }

        }
        else
        {
            throw new CompilationException(2, "IDENTIFIER expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

        }

    }

    private void arithmeticExpression() throws IOException, CompilationException
    {
        term();
        arithmeticExpressionLine();
    }

    private void arithmeticExpressionLine() throws IOException, CompilationException
    {
        if (TokenClassification.SUM_OP == lookAhead.getClassification())
        {
            updateLookAhead();
            term();
            arithmeticExpressionLine();
        }

        else if (TokenClassification.SUBTRACTION_OP == lookAhead.getClassification())
        {
            updateLookAhead();
            term();
            arithmeticExpressionLine();

        }

    }

    private boolean isRelacionalOp(Token token)
    {
        return TokenClassification.DIFERENT_FROM_COMPARATOR == token.
                getClassification() || TokenClassification.EQUAL_TO_COMPARATOR == token.
                getClassification() || TokenClassification.GREATER_THAN_COMPARATOR == token.
                getClassification() || TokenClassification.GREATER_THAN_OR_EQUAL_TO_COMPARATOR == token.
                getClassification() || TokenClassification.LESS_THAN_OR_EQUAL_TO_COMPARATOR == token.
                getClassification() || TokenClassification.LESS_THAN_COMPARATOR == token.
                getClassification();
    }

    private void term() throws IOException, CompilationException
    {
        factor();

        while (TokenClassification.DIVISION_OP == lookAhead.getClassification() || TokenClassification.MULTIPLICATION_OP == lookAhead.getClassification())
        {
            updateLookAhead();
            factor();
        }
    }

    private void factor() throws IOException, CompilationException
    {
        if (TokenClassification.LEFT_PARENTHESIS == lookAhead.getClassification())
        {
            updateLookAhead();
            arithmeticExpression();
            if (TokenClassification.RIGHT_PARENTHESIS == lookAhead.getClassification())
            {
                updateLookAhead();

            }
            else
            {
                throw new CompilationException(2, ")' or value or arithmetic operator expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

            }
        }
        else if (isValue(lookAhead))
        {
            updateLookAhead();
        }
        else
        {
            throw new CompilationException(2, "'(' or identifier or value expected, but token '" + lookAhead.getLexema() + "' was found. ", lineBeforeLastToken, collumnBeforeLastToken, lookAhead.getLexema());

        }
    }

    private boolean isValue(Token token)
    {
        return TokenClassification.CHARACTER == token.getClassification() || TokenClassification.INTEGER_VALUE == token.getClassification() || TokenClassification.FLOAT_VALUE == token.getClassification() || TokenClassification.IDENTIFIER == token.getClassification();

    }
}
