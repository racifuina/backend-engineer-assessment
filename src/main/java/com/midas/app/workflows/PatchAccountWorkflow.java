package com.midas.app.workflows;

import com.midas.app.models.Account;
import com.stripe.exception.StripeException;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.util.UUID;

@WorkflowInterface
public interface PatchAccountWorkflow {
  String QUEUE_NAME = "patch-account-workflow";

  /**
   * patchAccount updates firstName, lastName and email address of Account.
   *
   * @param accountId is the id of the Account to be patched.
   * @param details fields to be updated in the Account.
   * @return Account
   */
  @WorkflowMethod
  Account patchAccount(UUID accountId, Account details) throws StripeException;
}
