package com.midas.app.services;

import com.midas.app.mappers.Mapper;
import com.midas.app.models.Account;
import com.midas.app.repositories.AccountRepository;
import com.midas.app.workflows.CreateAccountWorkflow;
import com.midas.app.workflows.PatchAccountWorkflow;
import com.midas.generated.model.PatchAccountRequestDto;
import com.stripe.exception.StripeException;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.workflow.Workflow;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
  private final Logger logger = Workflow.getLogger(AccountServiceImpl.class);

  private final WorkflowClient workflowClient;

  private final AccountRepository accountRepository;

  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(Account details) throws StripeException {
    var options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(CreateAccountWorkflow.QUEUE_NAME)
            .setWorkflowId(details.getEmail())
            .build();

    logger.info("initiating workflow to create account for email: {}", details.getEmail());

    var workflow = workflowClient.newWorkflowStub(CreateAccountWorkflow.class, options);

    logger.info("finished creating account for email: {}", details.getEmail());

    return workflow.createAccount(details);
  }

  /**
   * getAccounts returns a list of accounts.
   *
   * @return List<Account>
   */
  @Override
  public List<Account> getAccounts() {
    return accountRepository.findAll();
  }

  @Override
  public Account patchAccount(UUID id, PatchAccountRequestDto dto) throws StripeException {

    var options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(PatchAccountWorkflow.QUEUE_NAME)
            .setWorkflowId(id.toString())
            .build();

    logger.info("initiating workflow to patch account: {}", id.toString());

    var workflow = workflowClient.newWorkflowStub(PatchAccountWorkflow.class, options);

    Account targetAccount = new Account();
    Mapper.patchAccountFromDto(targetAccount, dto);

    return workflow.patchAccount(id, targetAccount);
  }
}
