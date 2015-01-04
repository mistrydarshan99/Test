package com.darshan.Test.model;

public class UserModel {
    private String emailName,emailSub,emailTime,readMsg,attachmentName;
    private boolean checked = false;

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getReadMsg() {
        return readMsg;
    }

    public void setReadMsg(String readMsg) {
        this.readMsg = readMsg;
    }

    public String getEmailName() {
        return emailName;
    }

    public void setEmailName(String emailName) {
        this.emailName = emailName;
    }

    public String getEmailSub() {
        return emailSub;
    }

    public void setEmailSub(String emailSub) {
        this.emailSub = emailSub;
    }

    public String getEmailTime() {
        return emailTime;
    }

    public void setEmailTime(String emailTime) {
        this.emailTime = emailTime;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void toggleChecked() {
        checked = !checked;
    }
}
