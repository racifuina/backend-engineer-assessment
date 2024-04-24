package com.midas.app.activities;

import com.midas.app.mappers.Mapper;
import com.midas.app.models.Account;
import com.midas.app.providers.external.stripe.StripePaymentProvider;
import com.midas.app.repositories.AccountRepository;
import com.stripe.exception.StripeException;
import io.temporal.spring.boot.ActivityImpl;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ActivityImpl(taskQueues = {"create-account-workflow"})
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
  public Account createPaymentAccount(Account account) throws StripeException {
    logger.info("initiating createPaymentAccount {}", account.getEmail());

    return stripePaymentProvider.createAccount(Mapper.toCreateAccount(account));
  }
}
