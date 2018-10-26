package com.usaa.bank.graph.common.validate;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class Test_BasicValidator {
    final static String notNullString = "test";
    final static String emptyString = "";
    final static String isNullString = null;
    static String [] stringArray;
    static String [] stringArrayWithNulls;
    final static String [] emptyArray = new String[]{};

    @BeforeClass
    public static void setup(){
        stringArray=new String[]{notNullString,emptyString};
        stringArrayWithNulls=new String[]{notNullString,emptyString,isNullString};
    }

    @Test
    public void Test_validateNotNullGivenNotNullString(){
     assertEquals(BasicValidator.validateNotNull("String", notNullString), true);
    }
    @Test(expected = ValidationException.class)
    public void Test_validateNotNullGivenNullStringThrowsValidationException(){
        BasicValidator.validateNotNull("String", isNullString);
    }
    @Test
    public void Test_validateNotEmptyGivenNotNullString(){
        assertEquals(BasicValidator.validateNotEmpty("String", notNullString), true);
    }
    @Test(expected = ValidationException.class)
    public void Test_validateNotEmptyGivenNullStringThrowsValidationException(){
        BasicValidator.validateNotEmpty("String", isNullString);
    }
    @Test(expected = ValidationException.class)
    public void Test_validateNotEmptyGivenEmptyStringThrowsValidationException(){
        BasicValidator.validateNotEmpty("String", emptyString);
    }
    @Test
    public void Test_validateNoNullsGivenStringArray(){
        BasicValidator.validateNoNulls("String",stringArray);
    }

    @Test(expected = ValidationException.class)
    public void Test_validateNoNullsGivenStringArrayWithNullStringsThrowsValidationException(){
        BasicValidator.validateNoNulls("String", stringArrayWithNulls);
    }

    @Test
    public void Test_validateNotEmptyGivenStringArray(){
        BasicValidator.validateNotEmpty("String", stringArray);
    }

    @Test(expected = ValidationException.class)
    public void Test_validateNotEmptyGivenStringArrayWithEmptyStringThrowsValidationException(){
        BasicValidator.validateNotEmpty("String", emptyArray);
    }
    @Test
    public void Test_validateNotNegativeGivenPositiveInt(){
        BasicValidator.validateNotNegative("int", 5);
    }
    @Test(expected = ValidationException.class)
    public void Test_validateNotNegativeGivenNegativeInt(){
        BasicValidator.validateNotNegative("int", -5);
    }
    @Test
    public void Test_validateNotNegativeGivenPositiveDouble(){
        BasicValidator.validateNotNegative("double", 5);
    }
    @Test(expected = ValidationException.class)
    public void Test_validateNotNegativeGivenNegativeDouble(){
        BasicValidator.validateNotNegative("double", -5);
    }
    @Test
    public void Test_validateNotNegativeGivenPositiveLong(){
        BasicValidator.validateNotNegative("long", 5L);
    }
    @Test(expected = ValidationException.class)
    public void Test_validateNotNegativeGivenNegativeLong(){
        BasicValidator.validateNotNegative("long", -5L);
    }





}
