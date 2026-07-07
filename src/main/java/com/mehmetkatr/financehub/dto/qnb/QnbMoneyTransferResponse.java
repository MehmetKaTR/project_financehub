package com.mehmetkatr.financehub.dto.qnb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QnbMoneyTransferResponse {

    private String slipNumber;
    private String resultCode;
    private String resultDescription;
    private String inquiryNumber;
}
