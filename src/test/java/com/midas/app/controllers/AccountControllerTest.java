package com.midas.app.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.midas.app.models.Account;
import com.midas.app.services.AccountService;
import com.midas.generated.model.AccountDto;
import com.midas.generated.model.CreateAccountDto;
import com.midas.generated.model.PatchAccountRequestDto;
import com.stripe.exception.StripeException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class AccountControllerTest {
  @Mock private AccountService accountService;
  @InjectMocks private AccountController accountController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void createUserAccount() throws StripeException {
    String emailAddress = "email@test.com";
    String firstName = "Rigoberto";
    String lastName = "Acifuina";

    CreateAccountDto createAccountDto = new CreateAccountDto();
    createAccountDto.setEmail(emailAddress);
    createAccountDto.setFirstName(firstName);
    createAccountDto.setLastName(lastName);

    Account createdAccount = new Account();
    createdAccount.setEmail(emailAddress);
    createdAccount.setFirstName(firstName);
    createdAccount.setLastName(lastName);

    when(accountService.createAccount(any())).thenReturn(createdAccount);

    ResponseEntity<AccountDto> responseEntity =
        accountController.createUserAccount(createAccountDto);

    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertEquals(emailAddress, responseEntity.getBody().getEmail());
    assertEquals(firstName, responseEntity.getBody().getFirstName());
    assertEquals(lastName, responseEntity.getBody().getLastName());
  }

  @Test
  void getUserAccounts() {
    String emailAddress = "email@test.com";
    String firstName = "Rigoberto";
    String lastName = "Acifuina";

    Account account = new Account();
    account.setEmail(emailAddress);
    account.setFirstName(firstName);
    account.setLastName(lastName);

    when(accountService.getAccounts()).thenReturn(Collections.singletonList(account));

    ResponseEntity<List<AccountDto>> responseEntity = accountController.getUserAccounts();

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertEquals(1, responseEntity.getBody().size());
    assertEquals(emailAddress, responseEntity.getBody().get(0).getEmail());
    assertEquals(firstName, responseEntity.getBody().get(0).getFirstName());
    assertEquals(lastName, responseEntity.getBody().get(0).getLastName());
  }

  @Test
  void updateUserAccount() throws StripeException {
    UUID accountId = UUID.randomUUID();
    String updatedEmail = "new@test.com";

    PatchAccountRequestDto accountDto = new PatchAccountRequestDto();
    accountDto.setEmail(updatedEmail);

    Account updatedAccount = new Account();
    updatedAccount.setEmail(updatedEmail);

    when(accountService.patchAccount(any(), any())).thenReturn(updatedAccount);

    ResponseEntity<AccountDto> responseEntity =
        accountController.patchAccount(accountId, accountDto);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertEquals(updatedEmail, responseEntity.getBody().getEmail());
  }
}
