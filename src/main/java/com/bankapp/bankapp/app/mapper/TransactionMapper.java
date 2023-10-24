package com.bankapp.bankapp.app.mapper;

import com.bankapp.bankapp.app.dto.TransactionDto;
import com.bankapp.bankapp.app.dto.TransactionDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring",uses = UUID.class,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {
    Transaction transactionFUllDtoTotransaction(TransactionDtoFullUpdate transactionDtoFullUpdate);

    TransactionDto transactionToTransactionDto(Transaction transaction);
    List<TransactionDto> listTransactionToListTransactionDto(List<Transaction> transactions);
    Transaction mergeTransaction(Transaction from, @MappingTarget Transaction to);
}