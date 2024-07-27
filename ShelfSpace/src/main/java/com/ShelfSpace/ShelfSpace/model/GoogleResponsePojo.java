package com.ShelfSpace.ShelfSpace.model;

import lombok.Data;

@Data
public class GoogleResponsePojo {

    private String subtitle;
    private String authors;
    private String publishedDate;
    private String description;

    private String image;

    private String price;
    private String cureencyCode;

    private String synposis;

    private String bookPdf;
}
