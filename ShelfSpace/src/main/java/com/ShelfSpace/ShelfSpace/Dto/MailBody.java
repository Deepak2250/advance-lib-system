package com.ShelfSpace.ShelfSpace.Dto;

import lombok.Builder;

@Builder
public record MailBody(String to , String subject , String text ) {

}
