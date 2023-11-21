package com.bankapp.bankapp.app.mapper;

import com.bankapp.bankapp.app.dto.CardDto;
import com.bankapp.bankapp.app.dto.CardDtoPost;
import com.bankapp.bankapp.app.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = UUID.class)
public interface CardMapper {

    CardDto cardToCardDto(Card card);

    Card cardDtoToCard(CardDto cardDto);

    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    Card cardDtoPostToCard(CardDtoPost cardDtoPost);

    List<CardDto> listCardToListCardDto(List<Card> cards);
}