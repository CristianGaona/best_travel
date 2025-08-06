package com.best.travel.best_travel.infraestructure.helpers;

import org.springframework.stereotype.Component;

import com.best.travel.best_travel.util.exceptions.ForbiddenCustomerException;

@Component
public class BlockListHelper {
    

    public void isInBlackListCustomer(String customerId){
        if(customerId.equals("GOTW771012HMRGR087")){
            throw new ForbiddenCustomerException();
        }
    }
}
