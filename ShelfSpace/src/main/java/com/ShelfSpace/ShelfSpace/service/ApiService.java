package com.ShelfSpace.ShelfSpace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.ShelfSpace.ShelfSpace.model.GoogleBooksApiResponse;
import com.ShelfSpace.ShelfSpace.model.GoogleResponsePojo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApiService {

        @Autowired
        private RestTemplate restTemplate;

        @Value("${api.key}")
        private String apiKey;

        public GoogleResponsePojo getBookDetails(String title) {
                log.warn("We have entered in the Api service");
                apiKey = apiKey + title;
                ResponseEntity<GoogleBooksApiResponse> response = restTemplate.exchange(apiKey, HttpMethod.GET, null,
                                GoogleBooksApiResponse.class);
                GoogleBooksApiResponse apiResponse = response.getBody();

                if (apiResponse != null && apiResponse.getItems() != null && !apiResponse.getItems().isEmpty()) {
                        GoogleBooksApiResponse.BookItem bookItem = apiResponse.getItems().get(0);

                        // Map and return the book details
                        return mapToGoogleResponsePojo(bookItem);
                } else {
                        // Handle the case when there are no items or the response body is null
                        return new GoogleResponsePojo(); // Return an empty or default GoogleResponsePojo
                }
        }

        private GoogleResponsePojo mapToGoogleResponsePojo(GoogleBooksApiResponse.BookItem bookItem) {
                GoogleResponsePojo googleResponsePojo = new GoogleResponsePojo();

                // Map volumeInfo fields
                GoogleBooksApiResponse.BookItem.VolumeInfo volumeInfo = bookItem.getVolumeInfo();
                googleResponsePojo
                                .setSubtitle(volumeInfo.getSubtitle() != null ? volumeInfo.getSubtitle()
                                                : "Not Available");
                googleResponsePojo
                                .setAuthors(volumeInfo.getAuthors() != null ? volumeInfo.getAuthors().get(0)
                                                : "Not Available");
                googleResponsePojo.setPublishedDate(
                                volumeInfo.getPublishedDate() != null ? volumeInfo.getPublishedDate() : "No Available");
                googleResponsePojo.setDescription(
                                volumeInfo.getDescription() != null ? volumeInfo.getDescription() : "Not Available");
                googleResponsePojo
                                .setImage(volumeInfo.getImageLinks().getThumbnail() != null
                                                ? volumeInfo.getImageLinks().getThumbnail()
                                                : "Not Available");

                // Map saleInfo and price
                GoogleBooksApiResponse.BookItem.SaleInfo saleInfo = bookItem.getSaleInfo();
                if (saleInfo != null && saleInfo.getListPrice() != null) {
                        googleResponsePojo.setPrice(saleInfo.getListPrice().getAmount() != null
                                        ? saleInfo.getListPrice().getAmount()
                                        : "Not Available");
                        googleResponsePojo.setCureencyCode(saleInfo.getListPrice().getCurrencyCode() != null
                                        ? saleInfo.getListPrice().getCurrencyCode()
                                        : "Not Available");
                } else {
                        googleResponsePojo.setPrice("Not Available");
                        googleResponsePojo.setCureencyCode("Not Available");
                }

                // Map searchInfo
                GoogleBooksApiResponse.BookItem.SearchInfo searchInfo = bookItem.getSearchInfo();
                googleResponsePojo.setSynposis(searchInfo != null ? searchInfo.getTextSnippet() : "Not Available");

                // Map AccessInfo
                GoogleBooksApiResponse.AccessInfo accessInfo = bookItem.getAccessInfo();
                googleResponsePojo
                                .setBookPdf(accessInfo.getWebReaderLink() != null ? accessInfo.getWebReaderLink()
                                                : "Not Available");

                return googleResponsePojo;
        }

}
