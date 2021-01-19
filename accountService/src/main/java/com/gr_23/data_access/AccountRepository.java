package com.gr_23.data_access;

import com.gr_23.models.Customer;
import com.gr_23.models.User;

import java.util.HashMap;
import java.util.Map;

public class AccountRepository implements IAccountRepository {
    private Map<String, User> DTUpayUsers = new HashMap<>();

    @Override
    public Boolean addUser(User user) {
        if (!DTUpayUsers.containsKey(user.getUserId())) {
            DTUpayUsers.put(user.getUserId(), user);
            return true;
        }
        return false;
    }

    @Override
    public User fetchUser(String userId) {

        return DTUpayUsers.get(userId);
    }

    @Override
    public Boolean updateUser(User user) {
        User userTemp = DTUpayUsers.get(user.getUserId());
        if (userTemp == null) {
            return false;
        }
        userTemp.setFirstName(user.getFirstName());
        userTemp.setLastName(user.getLastName());
        return true;
    }

    @Override
    public Boolean deleteUsers(String userId) {
        if (DTUpayUsers.containsKey(userId)) {
            DTUpayUsers.remove(userId);
            return true;
        }
        return false;
    }

    @Override
    public Boolean userExist(String userId) {
        return DTUpayUsers.containsKey(userId);
    }
}
