package com.mehmetkatr.payment_service.dto.qnb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)   // null alanlari (addressType/addressValue) QNB'ye GONDERME
public class QnbMoneyTransferRequest {

    private Long accountNumber;
    private String targetAccount;

    private String currency;
    private String amount;

    private String receiverName;
    private String rentType;

    private String versionNumber;
    private Long firmReferansNumber;

    private int packedId;
    private String addressType;
    private String addressValue;
}
