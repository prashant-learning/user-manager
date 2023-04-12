package com.usbank.user.management.usermanager.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanAccountDetailsResponse {

    private String loanType;
    private String loanStatus;
    private double loanAmount;
    private double interestRate;
    private double emi;
    private double timeframe;
    private String accountId;
}
