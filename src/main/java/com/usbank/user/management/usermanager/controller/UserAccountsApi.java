package com.usbank.user.management.usermanager.controller;

import com.usbank.user.management.usermanager.model.Accounts;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserAccountsApi {

    public ResponseEntity<List<Accounts>> getUserAccountDetails(String username);
}
