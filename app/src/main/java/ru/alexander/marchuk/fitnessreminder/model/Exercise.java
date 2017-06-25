package ru.alexander.marchuk.fitnessreminder.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Exercise implements Item, Serializable {

    private String mTitle;
    private Date mDate;
    private UUID mId;
    private int mStatus;
    private long mTimeStamp;
    private int mDateStatus;

    public static final int STATUS_OVERDUE = 0;
    public static final int STATUS_EXERCISE = 1;
    public static final int STATUS_PROGRAMM = 2;

    public Exercise(){
        this(UUID.randomUUID());
    }

    public Exercise(UUID id){
        mDate = new Date();
        mStatus = -1;
        mId = id;
        mTimeStamp = new Date().getTime();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        mTimeStamp = timeStamp;
    }

    public int getDateStatus() {
        return mDateStatus;
    }

    public void setDateStatus(int dateStatus) {
        mDateStatus = dateStatus;
    }

    @Override
    public boolean isExercise() {
        return true;
    }
}
