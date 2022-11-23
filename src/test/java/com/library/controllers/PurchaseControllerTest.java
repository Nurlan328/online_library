package com.library.controllers;

import com.library.dtos.PurchaseRequestDTO;
import com.library.dtos.PurchaseResponseDTO;
import com.library.entities.Book;
import com.library.entities.Customer;
import com.library.entities.enums.PaymentMethod;
import com.library.facades.PurchaseFacade;
import org.assertj.core.util.Lists;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

public class PurchaseControllerTest {

    @InjectMocks
    private PurchaseController purchaseController;

    @Mock
    private PurchaseFacade purchaseFacade;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Add purchase in Bookshop controllers")
    public void addPurchaseTest() {
        //Given
        String email="johndoe@mailinator.com";
        Book book = Book.builder().isbn("ISBN00001").title("Book Name").price(9.99).author("Manuel").pages(200).provider("provider").build();

        Customer customer = Customer.builder().name("pedro").surname("pajares")
                .address("avenida palmera45").email(email)
                .id(1L).build();
        PurchaseRequestDTO purchaseRequestDto  = new PurchaseRequestDTO(customer.getEmail(),book.getIsbn(),PaymentMethod.PAYPAL.toString()); // creating the request
        PurchaseResponseDTO returnResult = purchaseFacade.performPurchase(purchaseRequestDto);

        given(purchaseFacade.performPurchase(purchaseRequestDto)).willReturn(returnResult);

        //When
        PurchaseResponseDTO returnResponse =  purchaseController.addPurchase(purchaseRequestDto);

        //Then
        assertEquals(returnResponse,returnResult);
    }

    @Test
    @DisplayName("Find purchases by customer´s email in Bookshop controllers")
    public void findPurchasesByCustomerEmailTest(){
        //Given
        String email="johndoe@mailinator.com";
        Book book = Book.builder().isbn("ISBN00001").title("Book Name").price(9.99).author("Manuel").pages(200).provider("provider").build();
        Customer customer = Customer.builder().name("pedro").surname("pajares")
                .address("avenida palmera45").email(email)
                .id(1L).build();
        Customer customer2 = Customer.builder().name("pedro").surname("pajares")
                .address("avenida palmera45").email(email)
                .id(1L).build();
        Customer customer3 = Customer.builder().name("pedro").surname("pajares")
                .address("avenida palmera45").email(email)
                .id(1L).build();

        PurchaseRequestDTO purchaseRequestDto  = new PurchaseRequestDTO(customer.getEmail(),book.getIsbn(),PaymentMethod.PAYPAL.toString());
        PurchaseRequestDTO purchaseRequestDto2  = new PurchaseRequestDTO(customer2.getEmail(),book.getIsbn(),PaymentMethod.PAYPAL.toString());
        PurchaseRequestDTO purchaseRequestDto3  = new PurchaseRequestDTO(customer3.getEmail(),book.getIsbn(),PaymentMethod.PAYPAL.toString());

        List<PurchaseResponseDTO> purchaseRequestDtoExpected =  Lists.list(purchaseController.addPurchase(purchaseRequestDto),purchaseController.addPurchase(purchaseRequestDto2),purchaseController.addPurchase(purchaseRequestDto3));

        given(purchaseFacade.findPurchasesByCustomerEmail(email)).willReturn(purchaseRequestDtoExpected);

        //When
        List<PurchaseResponseDTO> expectedPurchaseResponseDTO = purchaseController.findPurchasesByCustomerEmail(email);

        //Validation List
        List<PurchaseRequestDTO> booksIterator = new ArrayList<>();
        booksIterator.add(purchaseRequestDto);
        booksIterator.add(purchaseRequestDto2);
        booksIterator.add(purchaseRequestDto3);

        //Then
        assertAll( "Purchases found",
                () -> assertThat(expectedPurchaseResponseDTO, not(IsEmptyCollection.empty())),
                () -> assertThat(expectedPurchaseResponseDTO, hasSize(3)),
               () -> assertTrue(booksIterator.stream().allMatch(purch  -> (purch.getCustomerEmail().equals(email))))
        );

    }

}
