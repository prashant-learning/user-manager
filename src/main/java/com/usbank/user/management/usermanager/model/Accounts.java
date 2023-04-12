package com.usbank.user.management.usermanager.model;

import com.usbank.user.management.usermanager.model.response.LoanAccountDetailsResponse;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Accounts {

    private String accountId;
    private LocalDate openingDate;
    private String accountType; // joint account or single account
    private boolean isMinor;
    private String branchName;
    private String branchLocation;
    private String branchIFSC;

    private LoanAccountDetailsResponse loanAccountDetailsResponse;
}
