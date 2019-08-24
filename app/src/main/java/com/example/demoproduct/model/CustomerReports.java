package com.example.demoproduct.model;

public class CustomerReports {
    private String Comm;
    private String CustName;
    private String InvDate;
    private String InvNo;

    public String getInvNo() {
        return InvNo;
    }

    public String getComm() {
        return Comm;
    }

    public void setComm(String comm) {
        Comm = comm;
    }

    public String getQty() {
        return Qty;
    }

    public String getNetAmount() {
        return NetAmount;
    }

    public void setNetAmount(String netAmount) {
        NetAmount = netAmount;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public void setInvNo(String invNo) {
        InvNo = invNo;
    }



    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getInvDate() {
        return InvDate;
    }

    public void setInvDate(String invDate) {
        InvDate = invDate;
    }




    private String NetAmount;
    private String Qty;
}
