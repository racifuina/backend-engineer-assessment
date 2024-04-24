package com.midas.app.services;

import com.midas.app.models.Account;
import com.midas.generated.model.PatchAccountRequestDto;
import com.stripe.exception.StripeException;
import java.util.List;
import java.util.UUID;

public interface AccountService {
  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  Account createAccount(Account details) throws StripeException;

  /**
   * getAccounts returns a list of accounts.
   *
   * @return List<Account>
   */
  List<Account> getAccounts();

  /**
   * patchAccount set new values for firstName, lastName and email.
   *
   * @return Account
   */
  Account patchAccount(UUID id, PatchAccountRequestDto dto) throws StripeException;
}
