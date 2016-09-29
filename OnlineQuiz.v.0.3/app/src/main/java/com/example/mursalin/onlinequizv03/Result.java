package com.example.mursalin.onlinequizv03;


public class Result {

    String reg,ans;

    public Result(String reg,String ans) {
        this.reg = reg;
        this.ans = ans;
    }

    public String getAns() {
        int marks=0;
        for(int i=0;i<ans.length();i++){
            if(ans.charAt(i)=='1'){
                marks+=1;
            }
        }
        return String.valueOf(marks);
    }

    public String getReg() {
        return reg;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }
}
