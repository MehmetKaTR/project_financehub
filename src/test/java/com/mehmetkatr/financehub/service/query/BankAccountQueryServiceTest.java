package com.mehmetkatr.financehub.service.query;

import com.mehmetkatr.financehub.domain.Money;
import com.mehmetkatr.financehub.dto.response.BankAccountResponse;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.entity.User;
import com.mehmetkatr.financehub.mapper.BankAccountMapper;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountQueryServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @InjectMocks
    private BankAccountQueryService queryService;

    private BankAccount hesap() {
        return BankAccount.builder()
                .id(1L)
                .user(User.builder().id(5L).build())
                .balance(new Money(new BigDecimal("500"), "TRY"))
                .build();
    }

    @Test
    void findById_kayitVarsa_responseDoner() {
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(hesap()));
        when(bankAccountMapper.toResponse(any())).thenReturn(new BankAccountResponse());

        Optional<BankAccountResponse> sonuc = queryService.findById(1L);

        assertThat(sonuc).isPresent();
    }

    @Test
    void findById_kayitYoksa_bosOptionalDoner() {
        when(bankAccountRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<BankAccountResponse> sonuc = queryService.findById(99L);

        assertThat(sonuc).isEmpty();
    }

    @Test
    void findByUserId_hesaplariListeler() {
        when(bankAccountRepository.findByUserId(5L)).thenReturn(List.of(hesap(), hesap()));
        when(bankAccountMapper.toResponse(any())).thenReturn(new BankAccountResponse());

        List<BankAccountResponse> sonuc = queryService.findByUserId(5L);

        assertThat(sonuc).hasSize(2);
    }
}
