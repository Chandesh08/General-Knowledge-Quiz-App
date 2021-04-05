package com.example.dashbard;

public class Question {
    private int mTextRestID;
    private String mTextRest;
    private String mAnswerTrues;
public Question(){}
    public Question(int mTextRestID, String mTextRest, String mAnswerTrues) {
        this.mTextRestID = mTextRestID;
        this.mTextRest = mTextRest;
        this.mAnswerTrues = mAnswerTrues;
    }

    public int getmTextRestID() {
        return mTextRestID;
    }

    public void setmTextRestID(int mTextRestID) {
        this.mTextRestID = mTextRestID;
    }

    public String getmTextRest() {
        return mTextRest;
    }

    public void setmTextRest(String mTextRest) {
        this.mTextRest = mTextRest;
    }

    public String ismAnswerTrues() {
        return mAnswerTrues;
    }

    public void setmAnswerTrues(String mAnswerTrues) {
        this.mAnswerTrues = mAnswerTrues;
    }
    @Override
    public String toString() {
        return "Question{" +
                "mTextRestID=" + mTextRestID +
                ", mTextRest='" + mTextRest + '\'' +
                ", mAnswerTrues=" + mAnswerTrues +
                '}';
    }
}

