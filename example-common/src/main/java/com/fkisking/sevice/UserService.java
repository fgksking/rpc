package com.fkisking.sevice;

import com.fkisking.User;

import javax.jws.soap.SOAPBinding;

public interface UserService {
    User getUser(String name);
}
