package com.midas.app.activities;

import com.midas.app.exceptions.ResourceNotFoundException;
import com.midas.app.mappers.Mapper;
import com.midas.app.models.Account;
import com.midas.app.providers.external.stripe.StripePaymentProvider;
import com.midas.app.repositories.AccountRepository;
import com.stripe.exception.StripeException;
import io.temporal.spring.boot.ActivityImpl;
import io.temporal.workflow.Workflow;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ActivityImpl(taskQueues = {"create-account-workflow", "patch-account-workflow"})
public class AccountActivityImpl implements AccountActivity {
  private final Logger logger = Workflow.getLogger(AccountActivityImpl.class);

  private final AccountRepository repository;
  private final StripePaymentProvider stripePaymentProvider;

  @Override
  public Account saveAccount(Account account) {
    logger.info("Started saving account for email: {}", account.getEmail());
    return repository.save(account);
  }

  @Override
  public Account patchAccount(UUID accountId, Account modifiedAccount) {
    logger.info("patchAccount: {}", accountId);

    Account targetAccount =
        repository
            .findById(accountId)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));
    if (targetAccount != null) {
      Mapper.patchAccount(targetAccount, modifiedAccount);
      return repository.save(targetAccount);
    }
    return null;
  }

  @Override
  public Account createPaymentAccount(Account account) throws StripeException {
    logger.info("initiating createPaymentAccount {}", account.getEmail());

    return stripePaymentProvider.createAccount(Mapper.toCreateAccount(account));
  }

  @Override
  public Account patchPaymentAccount(Account account) throws StripeException {
    if (account == null) {
      throw new ResourceNotFoundException("Account not found");
    }
    return stripePaymentProvider.patchAccount(account);
  }
}
