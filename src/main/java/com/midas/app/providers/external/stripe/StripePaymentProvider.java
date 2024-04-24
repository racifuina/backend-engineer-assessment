package com.midas.app.providers.external.stripe;

import com.midas.app.models.Account;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Getter
public class StripePaymentProvider implements PaymentProvider {
  private final Logger logger = LoggerFactory.getLogger(StripePaymentProvider.class);

  private final StripeConfiguration configuration;

  public StripePaymentProvider(StripeConfiguration configuration) {
    this.configuration = configuration;
    Stripe.apiKey = configuration.getApiKey();
  }

  /** providerName is the name of the payment provider */
  @Override
  public String providerName() {
    return "stripe";
  }

  /**
   * createAccount creates a new account in the payment provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(CreateAccount details) throws StripeException {
    CustomerCreateParams params =
        CustomerCreateParams.builder()
            .setName(details.getFirstName().concat(" ").concat(details.getLastName()))
            .setEmail(details.getEmail())
            .build();
    Customer customer = Customer.create(params);
    return Account.builder()
        .providerId(customer.getId())
        .providerType(providerName())
        .firstName(details.getFirstName())
        .lastName(details.getLastName())
        .email(details.getEmail())
        .build();
  }

  @Override
  public Account patchAccount(Account details) throws StripeException {
    CustomerUpdateParams customerUpdateParams =
        CustomerUpdateParams.builder()
            .setName(details.getFirstName() + " " + details.getLastName())
            .setEmail(details.getEmail())
            .build();
    Customer customer = Customer.retrieve(details.getProviderId());
    customer.update(customerUpdateParams);
    return details;
  }
}
