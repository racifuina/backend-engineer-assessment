package com.midas.app.mappers;

import com.midas.app.models.Account;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.generated.model.AccountDto;
import com.midas.generated.model.PatchAccountRequestDto;
import lombok.NonNull;

public class Mapper {
  // Prevent instantiation
  private Mapper() {}

  /**
   * toAccountDto maps an account to an account dto.
   *
   * @param account is the account to be mapped
   * @return AccountDto
   */
  public static AccountDto toAccountDto(@NonNull Account account) {
    var accountDto = new AccountDto();

    accountDto
        .id(account.getId())
        .firstName(account.getFirstName())
        .lastName(account.getLastName())
        .email(account.getEmail())
        .providerId(account.getProviderId())
        .providerType(AccountDto.ProviderTypeEnum.STRIPE)
        .createdAt(account.getCreatedAt())
        .updatedAt(account.getUpdatedAt());

    return accountDto;
  }

  public static CreateAccount toCreateAccount(@NonNull Account account) {
    return CreateAccount.builder()
        .firstName(account.getFirstName())
        .lastName(account.getLastName())
        .email(account.getEmail())
        .build();
  }

  public static void patchAccount(Account targetAccount, Account modifiedAccount) {
    if (modifiedAccount.getFirstName() != null) {
      targetAccount.setFirstName(modifiedAccount.getFirstName());
    }
    if (modifiedAccount.getLastName() != null) {
      targetAccount.setLastName(modifiedAccount.getLastName());
    }
    if (modifiedAccount.getEmail() != null) {
      targetAccount.setEmail(modifiedAccount.getEmail());
    }
  }

  public static void patchAccountFromDto(Account targetAccount, PatchAccountRequestDto dto) {
    if (dto.getFirstName() != null) {
      targetAccount.setFirstName(dto.getFirstName());
    }
    if (dto.getLastName() != null) {
      targetAccount.setLastName(dto.getLastName());
    }
    if (dto.getEmail() != null) {
      targetAccount.setEmail(dto.getEmail());
    }
  }
}
