package com.bankapp.bankapp.app.mapper;

import com.bankapp.bankapp.app.dto.AccountDto;
import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;
import com.bankapp.bankapp.app.dto.AccountDtoPost;
import com.bankapp.bankapp.app.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = UUID.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {

    AccountDto accountToAccountDTO(Account account);

    Account accountDTOtoAccount(AccountDto accountDto);

    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    Account accountDtoPostToAccount(AccountDtoPost accountDtoPost);

    Account mergeAccounts(Account from, @MappingTarget Account to);

    Account accountDtoFullToAccount(AccountDtoFullUpdate accountDtoFullUpdate);

    List<AccountDto> ListAccountToListAccountDto(List<Account> accounts);

    @Mapping(target = "clientId",source = "client.id")
    AccountDtoFullUpdate accountToAccountFullDto(Account updated);
}
