package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sample.Rule;

public class RuleTest {


    @Test
    void ifBinaryRepresentationEqual30ReturnTrue(){
        Assertions.assertArrayEquals(new boolean[]{false,false,false,true,true,true,true,false},Rule.createValueRep("11110"));
    }

    @Test
    void ifBinaryRepresentationEqual60ReturnTrue(){
        Assertions.assertArrayEquals(new boolean[]{false,false,true,true,true,true,false,false},Rule.createValueRep("111100"));
    }

    @Test
    void ifBinaryRepresentationEqual90ReturnTrue(){
        Assertions.assertArrayEquals(new boolean[]{false,true,false,true,true,false,true,false},Rule.createValueRep("1011010"));
    }

//    @Test
//    void ifBinaryRepresentationEqual120ReturnTrue(){
//        Assertions.assertArrayEquals(new boolean[]{false,false,false,true,true,true,true,false},Rule.createValueRep("11110"));
//    }

    @Test
    void ifBinaryRepresentationEqual250ReturnTrue(){
        Assertions.assertArrayEquals(new boolean[]{true,true,true,true,true,false,true,false},Rule.createValueRep("11111010"));
    }
}
