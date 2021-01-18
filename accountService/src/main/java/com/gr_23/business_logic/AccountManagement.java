package com.gr_23.business_logic;


import com.gr_23.data_access.IAccountRepository;
import com.gr_23.models.Customer;
import com.gr_23.models.User;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import messaging.Event;
import messaging.EventReceiver;
import messaging.EventSender;

import java.util.HashMap;
import java.util.Map;

public class AccountManagement implements IAccountManagement, EventReceiver {
    EventSender sender;
    BankService bank = new BankServiceService().getBankServicePort();
    IAccountRepository accountRepository;

    public AccountManagement(EventSender sender, IAccountRepository accountRepository) {
        this.sender = sender;
        this.accountRepository = accountRepository;
    }

    @Override
    public Boolean createDTUPayAccount(Customer user)  {
       return accountRepository.addUser(user);
    }

    @Override
    public User fetchUser(String userId) {
        return accountRepository.fetchUser(userId);
    }

    @Override
    public boolean validateBankAccount(String CPR) {
        try {
            bank.getAccountByCprNumber(CPR);
            return true;
        } catch (BankServiceException_Exception e) {
            return false;
        }
    }

    @Override
    public boolean validateDTUPayAccount(String userId) {

        return accountRepository.userExist(userId);
    }

    @Override
    public Boolean deleteUsers(String userId) {

        return accountRepository.deleteUsers(userId);
    }

    @Override
    public Boolean updateUser(User user) {
        return accountRepository.updateUser(user);
    }
    void answerRequest_fetchUser(String eventType, String userId)
    {
        User user = fetchUser(userId);
        answerRequest(eventType +"_done",user.getCprNumber());
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
