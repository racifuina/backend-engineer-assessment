package com.midas.app.activities;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.midas.app.exceptions.ResourceNotFoundException;
import com.midas.app.models.Account;
import com.midas.app.providers.external.stripe.StripePaymentProvider;
import com.midas.app.repositories.AccountRepository;
import com.stripe.exception.StripeException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AccountActivityImplTest {
  @Mock private AccountRepository accountRepository;
  @Mock private StripePaymentProvider stripePaymentProvider;
  @InjectMocks private AccountActivityImpl accountActivity;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void saveAccount() {
    Account account = new Account();
    account.setEmail("rigoberto@test.com");

    when(accountRepository.save(account)).thenReturn(account);
    Account savedAccount = accountActivity.saveAccount(account);

    assertEquals(account, savedAccount);
    verify(accountRepository, times(1)).save(account);
  }

  @Test
  void patchAccount() {
    UUID accountId = UUID.randomUUID();
    String newEmail = "original@test.com";

    Account existingAccount = new Account();
    existingAccount.setId(accountId);
    existingAccount.setEmail(newEmail);

    Account updatedAccount = new Account();
    updatedAccount.setId(accountId);
    updatedAccount.setEmail(newEmail);

    when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
    when(accountRepository.save(existingAccount)).thenReturn(updatedAccount);

    Account result = accountActivity.patchAccount(accountId, updatedAccount);

    assertEquals(updatedAccount, result);
    assertEquals(newEmail, result.getEmail());
    verify(accountRepository, times(1)).findById(accountId);
    verify(accountRepository, times(1)).save(existingAccount);
  }

  @Test
  void createPaymentAccount() throws StripeException {
    String emailAddress = "email@test.com";

    Account account = new Account();
    account.setEmail(emailAddress);

    Account createdAccount = new Account();
    createdAccount.setEmail(emailAddress);

    when(stripePaymentProvider.createAccount(any())).thenReturn(createdAccount);

    Account result = accountActivity.createPaymentAccount(account);

    assertEquals(createdAccount, result);
    verify(stripePaymentProvider, times(1)).createAccount(any());
  }

  @Test
  void patchPaymentAccount() throws StripeException {
    String emailAddress = "email@test.com";

    Account account = new Account();
    account.setEmail(emailAddress);

    Account updatedAccount = new Account();
    updatedAccount.setEmail(emailAddress);

    when(stripePaymentProvider.patchAccount(account)).thenReturn(updatedAccount);

    Account result = accountActivity.patchPaymentAccount(account);

    assertEquals(updatedAccount, result);

    verify(stripePaymentProvider, times(1)).patchAccount(account);
  }

  @Test
  void updatePaymentAccount_NullAccount() {
    assertThrows(ResourceNotFoundException.class, () -> accountActivity.patchPaymentAccount(null));
  }
}
