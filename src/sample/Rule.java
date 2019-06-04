package sample;

public interface Rule {

    int RULE_30= 30;
    int RULE_60= 60;
    int RULE_90= 90;
    int RULE_150= 150;
    int RULE_250= 250;

    static boolean[] createValueRep(String rule){
        boolean [] valueRepr = new boolean[8];
        int counter=8-rule.length();

        for(int i=0;i<rule.length();i++){
            if(rule.charAt(i)=='1') valueRepr[counter]=true;
            counter++;
        }

        return valueRepr;
    }
}
