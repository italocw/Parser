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
public enum TokenClassification
{
    LEFT_PARENTHESIS(1),
    RIGHT_PARENTHESIS(2),
    LEFT_CURLY_BRACKET(3),
    RIGHT_CURLY_BRACKET(4),
    SEMICOLON(5),
    COMMA(6),
    SUM_OP(7),
    SUBTRACTION_OP(8),
    MULTIPLICATION_OP(9),
    EOF_CHARACTER(10),
    MULTIPLE_LINES_COMMENT(11),
    ONE_LINE_COMMENT(12),
    DIVISION_OP(13),
    DIFERENT_FROM_COMPARATOR(14),
    GREATER_THAN_OR_EQUAL_TO_COMPARATOR(15),
    GREATER_THAN_COMPARATOR(16),
    LESS_THAN_OR_EQUAL_TO_COMPARATOR(17),
    LESS_THAN_COMPARATOR(18),
    EQUAL_TO_COMPARATOR(19),
    ATRIBUITION_SIGN(20),
    IDENTIFIER(21),
    RESERVED_WORD_MAIN(22),
    RESERVED_WORD_IF(23),
    RESERVED_WORD_ELSE(24),
    RESERVED_WORD_WHILE(25),
    RESERVED_WORD_DO(26),
    RESERVED_WORD_FOR(27),
    RESERVED_WORD_INT(28),
    RESERVED_WORD_FLOAT(29),
    RESERVED_WORD_CHAR(30),
    CHARACTER(31),
    INTEGER_VALUE(32),
    FLOAT_VALUE(33);

    private int classificationNumber;

    TokenClassification(int classificationNumber)
    {
        this.classificationNumber = classificationNumber;
    }

    int getClassificationNumber()
    {
        return classificationNumber;
    }
}