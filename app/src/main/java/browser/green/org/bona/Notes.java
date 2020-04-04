package browser.green.org.bona;

import java.io.Serializable;

public class Notes implements Serializable {
    public String mContext;
    public String EditDate;
    public String RemindDate;
    public Integer mID;
    public Integer mIsClocked=0;

    public Integer getIsClocked(){return mIsClocked;}

    public void setIsClocked(Integer mIsClocked){this.mIsClocked=mIsClocked;}

    public Notes(){}

    public Notes(String mContext, String EditDate, String RemindDate){
        this.mContext=mContext;
        this.EditDate=EditDate;
        this.RemindDate=RemindDate;
    }

    public String getContext(){
        return  mContext;
    }

    public void setContext(String mContext){
        this.mContext=mContext;
    }

    public Integer getID(){return mID;}

    public void setID(Integer mID){this.mID=mID;}

    public String getEditDate(){
        return EditDate;
    }

    public void setEditDate(String EditDate){
        this.EditDate=EditDate;
    }

    public String getRemindDate(){
        return RemindDate;
    }

    public  void setRemindDate(String RemindDate){
        this.RemindDate=RemindDate;
    }


}
