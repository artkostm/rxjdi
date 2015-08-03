package by.artkostm.rxj.util;

public class Reflections {
    
    public static final String SETTER_PREFIX = "set";
    
    public static String getSetterName(String fieldName){
        final String firstLetter = String.valueOf(fieldName.charAt(0)).toUpperCase();
        final String setter = SETTER_PREFIX + firstLetter + fieldName.substring(1);
        return setter;
    }
    
    public static void main(String[] args){
        System.out.println(getSetterName("f1"));
    }
}
