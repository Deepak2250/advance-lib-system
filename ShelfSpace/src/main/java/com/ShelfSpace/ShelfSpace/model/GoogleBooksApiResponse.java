package com.ShelfSpace.ShelfSpace.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.List;

@Data
public class GoogleBooksApiResponse {
    @JsonProperty("items")
    private List<BookItem> items; // This is the Super Class or Super Array of the Json Response ;

    @Data
    public static class BookItem {
        @JsonProperty("volumeInfo")
        private VolumeInfo volumeInfo; // All the Class or Sub arrays of the Json Response

        @JsonProperty("saleInfo")
        private SaleInfo saleInfo; // All the Class or Sub arrays of the Json Response

        @JsonProperty("searchInfo")
        private SearchInfo searchInfo; // All the Class or Sub arrays of the Json Response

        @JsonProperty("accessInfo")
        private AccessInfo accessInfo; // All the Class or Sub arrays of the Json Response

        // Under This Line All The Classes Are the Sub classes of the Sub classes or sub
        // array of the Json Response
        @Data
        public static class VolumeInfo {
            @JsonProperty("subtitle")
            private String subtitle;

            @JsonProperty("authors")
            private List<String> authors;

            @JsonProperty("publishedDate")
            private String publishedDate;

            @JsonProperty("description")
            private String description;

            @JsonProperty("imageLinks")
            private ImageLinks imageLinks;

            @Data
            public static class ImageLinks {
                @JsonProperty("thumbnail")
                private String thumbnail;
            }
        }

        @Data
        public static class SaleInfo {
            @JsonProperty("listPrice")
            private Price listPrice;

            @JsonProperty("currencyCode")
            private String currencyCode;

            @Data
            public static class Price {
                @JsonProperty("amount")
                private String amount;

                @JsonProperty("currencyCode")
                private String currencyCode;
            }
        }

        @Data
        public static class SearchInfo {
            @JsonProperty("textSnippet")
            private String textSnippet;
        }
    }

    @Data
    public static class AccessInfo {
        @JsonProperty("webReaderLink")
        private String webReaderLink;
    }
}
