package com.midas.app.workflows;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import com.stripe.exception.StripeException;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@RequiredArgsConstructor
@WorkflowImpl(taskQueues = "create-account-workflow")
public class CreateAccountWorkflowImpl implements CreateAccountWorkflow {
  private final Logger logger = Workflow.getLogger(CreateAccountWorkflowImpl.class);
  private final RetryOptions retryoptions =
      RetryOptions.newBuilder()
          .setInitialInterval(Duration.ofSeconds(1))
          .setMaximumInterval(Duration.ofSeconds(100))
          .setBackoffCoefficient(2)
          .setMaximumAttempts(1)
          .build();

  private final ActivityOptions options =
      ActivityOptions.newBuilder()
          .setStartToCloseTimeout(Duration.ofSeconds(30))
          .setRetryOptions(retryoptions)
          .build();

  private final AccountActivity activity = Workflow.newActivityStub(AccountActivity.class, options);

  @Override
  public Account createAccount(Account details) throws StripeException {
    logger.info("createAccount started ");
    details = activity.createPaymentAccount(details);
    logger.info("created payment account");
    activity.saveAccount(details);
    logger.info("saved account details");
    return details;
  }
}
