package com.midas.app.providers.payment;

import com.midas.app.models.Account;
import com.stripe.exception.StripeException;

public interface PaymentProvider {
  /** providerName is the name of the payment provider */
  String providerName();

  /**
   * createAccount creates a new account in the payment provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  Account createAccount(CreateAccount details) throws StripeException;

  /**
   * patchAccount updates an payment account. @Param accountId is the id of account needs to be
   * updated
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  Account patchAccount(Account details) throws StripeException;
}
