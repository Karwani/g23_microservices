package com.gr_23.business_logic;


import com.gr_23.Customer;
import com.gr_23.User;
import dtu.ws.fastmoney.BankServiceException_Exception;
import messaging.Event;
import messaging.EventReceiver;
import messaging.EventSender;

import javax.ws.rs.core.Response;

public class AccountManagement implements IAccountManagement, EventReceiver {
    EventSender sender;

    public AccountManagement(EventSender sender) {
        this.sender = sender;
    }

    @Override
    public Boolean createDTUPayAccount(Customer user) throws Exception {
        return null;
    }

    @Override
    public User fetchUser(String userId) {
        return null;
    }

    @Override
    public boolean validateBankAccount(String CPR) throws BankServiceException_Exception {
        return false;
    }

    @Override
    public boolean validateDTUPayAccount(String userId) {
        return false;
    }

    @Override
    public Boolean deleteUsers(String userId) {
        return null;
    }

    @Override
    public void updateUser(User user) {

    }
    void answerRequest_fetchUser(String eventType, String userId)
    {
        User user = fetchUser(userId);
        answerRequest(eventType +"_done",user);
    }
    void answerRequest_validateDTUPayAccount(String eventType, String userId)
    {
        boolean valid = validateDTUPayAccount(userId);
        answerRequest(eventType +"_done",valid);
    }
    @Override
    public void receiveEvent(Event event) {
        if(event.getEventType().equals("validateDTUPayAccount"))
        {
            String userId = event.getArgument(0,String.class);
            answerRequest_validateDTUPayAccount(event.getEventType(), userId);
        }
        if(event.getEventType().equals("fetchUser"))
        {
            String userId = event.getArgument(0,String.class);
            answerRequest_fetchUser(event.getEventType(), userId);
        }
    }
    public void answerRequest(String eventType, Object arg){
        Event event = new Event(eventType,new Object[] { arg });
        try {
            sender.sendEvent(event);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
