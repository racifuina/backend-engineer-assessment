package com.midas.app.workflows;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import com.stripe.exception.StripeException;
import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@RequiredArgsConstructor
@WorkflowImpl(taskQueues = "patch-account-workflow")
public class PatchAccountWorkflowImpl implements PatchAccountWorkflow {

  private final Logger logger = Workflow.getLogger(PatchAccountWorkflowImpl.class);

  private final AccountActivity patchAccountActivity =
      Workflow.newActivityStub(
          AccountActivity.class,
          ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build());

  @Override
  public Account patchAccount(UUID accountId, Account details) throws StripeException {
    logger.info("patchAccount started ");
    details = patchAccountActivity.patchAccount(accountId, details);
    logger.info("patched payment account");
    patchAccountActivity.patchPaymentAccount(details);
    logger.info("patched Payment Account Details");
    return details;
  }
}
