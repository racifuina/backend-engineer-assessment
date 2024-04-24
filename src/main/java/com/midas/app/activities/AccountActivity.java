package com.midas.app.activities;

import com.midas.app.models.Account;
import com.stripe.exception.StripeException;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import java.util.UUID;

@ActivityInterface
public interface AccountActivity {
  /**
   * saveAccount saves an account in the data store.
   *
   * @param account is the account to be saved
   * @return Account
   */
  @ActivityMethod
  Account saveAccount(Account account);

  @ActivityMethod
  Account patchAccount(UUID accountId, Account account);

  /**
   * createPaymentAccount creates a payment account in the system or provider.
   *
   * @param account is the account to be created
   * @return Account
   */
  @ActivityMethod
  Account createPaymentAccount(Account account) throws StripeException;

  @ActivityMethod
  Account patchPaymentAccount(Account account) throws StripeException;
}
