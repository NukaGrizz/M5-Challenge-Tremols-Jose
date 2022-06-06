package com.trilogyed.gamestoreinvoicing.service;

import com.trilogyed.gamestoreinvoicing.feign.GameStoreCatalog;
import com.trilogyed.gamestoreinvoicing.model.*;
import com.trilogyed.gamestoreinvoicing.repository.*;
import com.trilogyed.gamestoreinvoicing.viewModel.ConsoleViewModel;
import com.trilogyed.gamestoreinvoicing.viewModel.GameViewModel;
import com.trilogyed.gamestoreinvoicing.viewModel.InvoiceViewModel;
import com.trilogyed.gamestoreinvoicing.viewModel.TShirtViewModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class GameStoreServiceLayerTest {

    GameStoreCatalog gameStoreCatalog;
    InvoiceRepository invoiceRepository;
    ProcessingFeeRepository processingFeeRepository;
    TaxRepository taxRepository;
    GameStoreServiceLayer service;

    @Before
    public void setUp() throws Exception {
        setUpgameStoreCatalogMock();
        setUpInvoiceRepositoryMock();
        setUpProcessingFeeRepositoryMock();
        setUpTaxRepositoryMock();

        service = new GameStoreServiceLayer(invoiceRepository, taxRepository,
                processingFeeRepository, gameStoreCatalog);
    }

    //Testing Invoice Operations...
    @Test
    public void shouldCreateFindInvoice() {
//        TShirtViewModel tShirt = new TShirtViewModel();
//        tShirt.setSize("Medium");
//        tShirt.setColor("Blue");
//        tShirt.setDescription("V-Neck");
//        tShirt.setPrice(new BigDecimal("19.99"));
//        tShirt.setQuantity(5);
//        tShirt = service.createTShirt(tShirt);

        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NC");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("T-Shirt");
        invoiceViewModel.setItemId(54);
        invoiceViewModel.setQuantity(2);

        invoiceViewModel = service.createInvoice(invoiceViewModel);

        InvoiceViewModel ivmfromService = service.getInvoice(invoiceViewModel.getId());

        assertEquals(invoiceViewModel, ivmfromService);
    }

    @Test
    public void shouldFindAllInvoices(){
        InvoiceViewModel savedInvoice1 = new InvoiceViewModel();
        savedInvoice1.setName("Sandy Beach");
        savedInvoice1.setStreet("123 Main St");
        savedInvoice1.setCity("any City");
        savedInvoice1.setState("NY");
        savedInvoice1.setZipcode("10016");
        savedInvoice1.setItemType("T-Shirt");
        savedInvoice1.setItemId(12);//pretending item exists with this id...
        savedInvoice1.setUnitPrice(new BigDecimal("12.50"));//pretending item exists with this price...
        savedInvoice1.setQuantity(2);
        savedInvoice1.setSubtotal(savedInvoice1.getUnitPrice().multiply(new BigDecimal(savedInvoice1.getQuantity())));
        savedInvoice1.setTax(savedInvoice1.getSubtotal().multiply(new BigDecimal("0.06")));
        savedInvoice1.setProcessingFee(new BigDecimal("10.00"));
        savedInvoice1.setTotal(savedInvoice1.getSubtotal().add(savedInvoice1.getTax()).add(savedInvoice1.getProcessingFee()));
        savedInvoice1.setId(22);

        InvoiceViewModel savedInvoice2 = new InvoiceViewModel();
        savedInvoice2.setName("Rob Bank");
        savedInvoice2.setStreet("888 Main St");
        savedInvoice2.setCity("any town");
        savedInvoice2.setState("NJ");
        savedInvoice2.setZipcode("08234");
        savedInvoice2.setItemType("Console");
        savedInvoice2.setItemId(120);//pretending item exists with this id...
        savedInvoice2.setUnitPrice(new BigDecimal("129.50"));//pretending item exists with this price...
        savedInvoice2.setQuantity(1);
        savedInvoice2.setSubtotal(savedInvoice2.getUnitPrice().multiply(new BigDecimal(savedInvoice2.getQuantity())));
        savedInvoice2.setTax(savedInvoice2.getSubtotal().multiply(new BigDecimal("0.08")));
        savedInvoice2.setProcessingFee(new BigDecimal("10.00"));
        savedInvoice2.setTotal(savedInvoice2.getSubtotal().add(savedInvoice2.getTax()).add(savedInvoice2.getProcessingFee()));
        savedInvoice2.setId(12);

        InvoiceViewModel savedInvoice3 = new InvoiceViewModel();
        savedInvoice3.setName("Sandy Beach");
        savedInvoice3.setStreet("123 Broad St");
        savedInvoice3.setCity("any where");
        savedInvoice3.setState("CA");
        savedInvoice3.setZipcode("90016");
        savedInvoice3.setItemType("Game");
        savedInvoice3.setItemId(19);//pretending item exists with this id...
        savedInvoice3.setUnitPrice(new BigDecimal("12.50"));//pretending item exists with this price...
        savedInvoice3.setQuantity(4);
        savedInvoice3.setSubtotal(savedInvoice3.getUnitPrice().multiply(new BigDecimal(savedInvoice3.getQuantity())));
        savedInvoice3.setTax(savedInvoice3.getSubtotal().multiply(new BigDecimal("0.09")));
        savedInvoice3.setProcessingFee(BigDecimal.ZERO);
        savedInvoice3.setTotal(savedInvoice3.getSubtotal().add(savedInvoice3.getTax()).add(savedInvoice3.getProcessingFee()));
        savedInvoice3.setId(73);

        List<InvoiceViewModel> currInvoices = new ArrayList<>();
        currInvoices.add(savedInvoice1);
        currInvoices.add(savedInvoice2);
        currInvoices.add(savedInvoice3);

        List<InvoiceViewModel> foundAllInvoices = service.getAllInvoices();

        assertEquals(currInvoices.size(), foundAllInvoices.size());
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldFailCreateFindInvoiceWithBadState() {

        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("T-Shirt");
        invoiceViewModel.setItemId(99);
        invoiceViewModel.setQuantity(2);

        invoiceViewModel = service.createInvoice(invoiceViewModel);

        InvoiceViewModel ivmfromService = service.getInvoice(invoiceViewModel.getId());

        assertEquals(invoiceViewModel, ivmfromService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailCreateFindInvoiceWithBadItemType() {


        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NC");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("Bad Item Type");
        invoiceViewModel.setItemId(54);
        invoiceViewModel.setQuantity(2);

        invoiceViewModel = service.createInvoice(invoiceViewModel);

        InvoiceViewModel ivmfromService = service.getInvoice(invoiceViewModel.getId());

        assertEquals(invoiceViewModel, ivmfromService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailCreateFindInvoiceWithNoInventory() {

        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NC");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("T-Shirt");
        invoiceViewModel.setItemId(54);
        invoiceViewModel.setQuantity(6);

        invoiceViewModel = service.createInvoice(invoiceViewModel);

        InvoiceViewModel ivmfromService = service.getInvoice(invoiceViewModel.getId());

        assertEquals(invoiceViewModel, ivmfromService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenCreateInvoiceInvalidItem() {

        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NC");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("nothing");
        invoiceViewModel.setItemId(54);
        invoiceViewModel.setQuantity(2);

        invoiceViewModel = service.createInvoice(invoiceViewModel);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenCreateInvoiceInvalidQuantity() {

        InvoiceViewModel invoiceViewModel = new InvoiceViewModel();
        invoiceViewModel.setName("John Jake");
        invoiceViewModel.setStreet("street");
        invoiceViewModel.setCity("Charlotte");
        invoiceViewModel.setState("NC");
        invoiceViewModel.setZipcode("83749");
        invoiceViewModel.setItemType("T-Shirt");
        invoiceViewModel.setItemId(54);
        invoiceViewModel.setQuantity(0);

        invoiceViewModel = service.createInvoice(invoiceViewModel);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWhenCreateInvoiceInvalidInvoiceMV() {

        InvoiceViewModel invoiceViewModel = null;

        invoiceViewModel = service.createInvoice(invoiceViewModel);
    }

    //DAO Mocks...
    private void setUpgameStoreCatalogMock() {

        gameStoreCatalog = mock(GameStoreCatalog.class);


        //Console Mocks
        ConsoleViewModel newConsole1 = new ConsoleViewModel();
        newConsole1.setModel("Playstation");
        newConsole1.setManufacturer("Sony");
        newConsole1.setMemoryAmount("120gb");
        newConsole1.setProcessor("Intel I7-9750H");
        newConsole1.setPrice(new BigDecimal("299.99"));
        newConsole1.setQuantity(4);

        ConsoleViewModel savedConsole1 = new ConsoleViewModel();
        savedConsole1.setId(40);
        savedConsole1.setModel("Playstation");
        savedConsole1.setManufacturer("Sony");
        savedConsole1.setMemoryAmount("120gb");
        savedConsole1.setProcessor("Intel I7-9750H");
        savedConsole1.setPrice(new BigDecimal("299.99"));
        savedConsole1.setQuantity(4);

        doReturn(savedConsole1).when(gameStoreCatalog).getConsole(40L);

        //Game Mocks
        GameViewModel newGame1 = new GameViewModel();
        newGame1.setTitle("Halo");
        newGame1.setEsrbRating("E10+");
        newGame1.setDescription("Puzzles and Math");
        newGame1.setPrice(new BigDecimal("23.99"));
        newGame1.setStudio("Xbox Game Studios");
        newGame1.setQuantity(5);

        GameViewModel savedGame1 = new GameViewModel();
        savedGame1.setId(32);
        savedGame1.setTitle("Halo");
        savedGame1.setEsrbRating("E10+");
        savedGame1.setDescription("Puzzles and Math");
        savedGame1.setPrice(new BigDecimal("23.99"));
        savedGame1.setStudio("Xbox Game Studios");
        savedGame1.setQuantity(5);

        GameViewModel newGame2 = new GameViewModel();
        newGame2.setTitle("Tetris");
        newGame2.setEsrbRating("E10+");
        newGame2.setDescription("block puzzle game");
        newGame2.setPrice(new BigDecimal("10.99"));
        newGame2.setStudio("Dolby Studios");
        newGame2.setQuantity(9);

        GameViewModel savedGame2 = new GameViewModel();
        savedGame2.setId(25);
        savedGame2.setTitle("Tetris");
        savedGame2.setEsrbRating("E10+");
        savedGame2.setDescription("block puzzle game");
        savedGame2.setPrice(new BigDecimal("10.99"));
        savedGame2.setStudio("Dolby Studios");
        savedGame2.setQuantity(9);

        GameViewModel newGame3 = new GameViewModel();
        newGame3.setTitle("Fort Lines");
        newGame3.setEsrbRating("M");
        newGame3.setDescription("Zombie shooter game");
        newGame3.setPrice(new BigDecimal("37.99"));
        newGame3.setStudio("Dolby Studios");
        newGame3.setQuantity(3);

        GameViewModel savedGame3 = new GameViewModel();
        savedGame3.setId(60);
        savedGame3.setTitle("Fort Lines");
        savedGame3.setEsrbRating("M");
        savedGame3.setDescription("Zombie shooter game");
        savedGame3.setPrice(new BigDecimal("37.99"));
        savedGame3.setStudio("Dolby Studios");
        savedGame3.setQuantity(3);

        doReturn(savedGame3).when(gameStoreCatalog).getGame(60L);
        doReturn(savedGame1).when(gameStoreCatalog).getGame(32L);
        doReturn(savedGame2).when(gameStoreCatalog).getGame(25L);

        //TShirt mocks
        TShirtViewModel newTShirt1 = new TShirtViewModel();
        newTShirt1.setSize("Medium");
        newTShirt1.setColor("Blue");
        newTShirt1.setDescription("V-Neck");
        newTShirt1.setPrice(new BigDecimal("19.99"));
        newTShirt1.setQuantity(5);

        TShirtViewModel savedTShirt1 = new TShirtViewModel();
        savedTShirt1.setId(54);
        savedTShirt1.setSize("Medium");
        savedTShirt1.setColor("Blue");
        savedTShirt1.setDescription("V-Neck");
        savedTShirt1.setPrice(new BigDecimal("19.99"));
        savedTShirt1.setQuantity(5);

        TShirtViewModel newTShirt2 = new TShirtViewModel();
        newTShirt2.setSize("Large");
        newTShirt2.setColor("Blue");
        newTShirt2.setDescription("long sleeve");
        newTShirt2.setPrice(new BigDecimal("30.99"));
        newTShirt2.setQuantity(8);

        TShirtViewModel savedTShirt2 = new TShirtViewModel();
        savedTShirt2.setId(60);
        savedTShirt2.setSize("Large");
        savedTShirt2.setColor("Blue");
        savedTShirt2.setDescription("long sleeve");
        savedTShirt2.setPrice(new BigDecimal("30.99"));
        savedTShirt2.setQuantity(8);

        TShirtViewModel newTShirt3 = new TShirtViewModel();
        newTShirt3.setSize("Medium");
        newTShirt3.setColor("orange");
        newTShirt3.setDescription("sleeveless");
        newTShirt3.setPrice(new BigDecimal("15.99"));
        newTShirt3.setQuantity(3);

        TShirtViewModel savedTShirt3 = new TShirtViewModel();
        savedTShirt3.setId(72);
        savedTShirt3.setSize("Medium");
        savedTShirt3.setColor("orange");
        savedTShirt3.setDescription("sleeveless");
        savedTShirt3.setPrice(new BigDecimal("15.99"));
        savedTShirt3.setQuantity(3);

        TShirtViewModel newTShirt4 = new TShirtViewModel();
        newTShirt4.setSize("Small");
        newTShirt4.setColor("Red");
        newTShirt4.setDescription("sleeveless");
        newTShirt4.setPrice(new BigDecimal("400"));
        newTShirt4.setQuantity(30);

        TShirtViewModel savedTShirt4 = new TShirtViewModel();
        savedTShirt4.setId(99);
        savedTShirt4.setSize("Small");
        savedTShirt4.setColor("Red");
        savedTShirt4.setDescription("sleeveless");
        savedTShirt4.setPrice(new BigDecimal("400"));
        savedTShirt4.setQuantity(30);


        doReturn(savedTShirt3).when(gameStoreCatalog).getTShirt(72L);
        doReturn(savedTShirt1).when(gameStoreCatalog).getTShirt(54L);
        doReturn(savedTShirt4).when(gameStoreCatalog).getTShirt(99L);

    }

    private void setUpInvoiceRepositoryMock() {
        invoiceRepository = mock(InvoiceRepository.class);

        Invoice invoice = new Invoice();
        invoice.setName("John Jake");
        invoice.setStreet("street");
        invoice.setCity("Charlotte");
        invoice.setState("NC");
        invoice.setZipcode("83749");
        invoice.setItemType("T-Shirt");
        invoice.setItemId(54);
        invoice.setUnitPrice(new BigDecimal("19.99"));
        invoice.setQuantity(2);
        invoice.setSubtotal(new BigDecimal("39.98"));
        invoice.setTax(new BigDecimal("2"));
        invoice.setProcessingFee(new BigDecimal("1.98"));
        invoice.setTotal(new BigDecimal("43.96"));

        Invoice invoice1 = new Invoice();
        invoice1.setId(20);
        invoice1.setName("John Jake");
        invoice1.setStreet("street");
        invoice1.setCity("Charlotte");
        invoice1.setState("NC");
        invoice1.setZipcode("83749");
        invoice1.setItemType("T-Shirt");
        invoice1.setItemId(54);
        invoice1.setUnitPrice(new BigDecimal("19.99"));
        invoice1.setQuantity(2);
        invoice1.setSubtotal(new BigDecimal("39.98"));
        invoice1.setTax(new BigDecimal("2"));
        invoice1.setProcessingFee(new BigDecimal("1.98"));
        invoice1.setTotal(new BigDecimal("43.96"));

        doReturn(invoice1).when(invoiceRepository).save(invoice);
        doReturn(Optional.of(invoice1)).when(invoiceRepository).findById(20L);

        //Get All...
        Invoice savedInvoice1 = new Invoice();
        savedInvoice1.setName("Sandy Beach");
        savedInvoice1.setStreet("123 Main St");
        savedInvoice1.setCity("any City");
        savedInvoice1.setState("NY");
        savedInvoice1.setZipcode("10016");
        savedInvoice1.setItemType("T-Shirt");
        savedInvoice1.setItemId(12);//pretending item exists with this id...
        savedInvoice1.setUnitPrice(new BigDecimal("12.50"));//pretending item exists with this price...
        savedInvoice1.setQuantity(2);
        savedInvoice1.setSubtotal(savedInvoice1.getUnitPrice().multiply(new BigDecimal(savedInvoice1.getQuantity())));
        savedInvoice1.setTax(savedInvoice1.getSubtotal().multiply(new BigDecimal("0.06")));
        savedInvoice1.setProcessingFee(new BigDecimal("10.00"));
        savedInvoice1.setTotal(savedInvoice1.getSubtotal().add(savedInvoice1.getTax()).add(savedInvoice1.getProcessingFee()));
        savedInvoice1.setId(22);

        Invoice savedInvoice2 = new Invoice();
        savedInvoice2.setName("Rob Bank");
        savedInvoice2.setStreet("888 Main St");
        savedInvoice2.setCity("any town");
        savedInvoice2.setState("NJ");
        savedInvoice2.setZipcode("08234");
        savedInvoice2.setItemType("Console");
        savedInvoice2.setItemId(120);//pretending item exists with this id...
        savedInvoice2.setUnitPrice(new BigDecimal("129.50"));//pretending item exists with this price...
        savedInvoice2.setQuantity(1);
        savedInvoice2.setSubtotal(savedInvoice2.getUnitPrice().multiply(new BigDecimal(savedInvoice2.getQuantity())));
        savedInvoice2.setTax(savedInvoice2.getSubtotal().multiply(new BigDecimal("0.08")));
        savedInvoice2.setProcessingFee(new BigDecimal("10.00"));
        savedInvoice2.setTotal(savedInvoice2.getSubtotal().add(savedInvoice2.getTax()).add(savedInvoice2.getProcessingFee()));
        savedInvoice2.setId(12);

        Invoice savedInvoice3 = new Invoice();
        savedInvoice3.setName("Sandy Beach");
        savedInvoice3.setStreet("123 Broad St");
        savedInvoice3.setCity("any where");
        savedInvoice3.setState("CA");
        savedInvoice3.setZipcode("90016");
        savedInvoice3.setItemType("Game");
        savedInvoice3.setItemId(19);//pretending item exists with this id...
        savedInvoice3.setUnitPrice(new BigDecimal("12.50"));//pretending item exists with this price...
        savedInvoice3.setQuantity(4);
        savedInvoice3.setSubtotal(savedInvoice3.getUnitPrice().multiply(new BigDecimal(savedInvoice3.getQuantity())));
        savedInvoice3.setTax(savedInvoice3.getSubtotal().multiply(new BigDecimal("0.09")));
        savedInvoice3.setProcessingFee(BigDecimal.ZERO);
        savedInvoice3.setTotal(savedInvoice3.getSubtotal().add(savedInvoice3.getTax()).add(savedInvoice3.getProcessingFee()));
        savedInvoice3.setId(73);

        List<Invoice> allList = new ArrayList<>();
        allList.add(savedInvoice1);
        allList.add(savedInvoice2);
        allList.add(savedInvoice3);

        doReturn(allList).when(invoiceRepository).findAll();
    }

    private void setUpProcessingFeeRepositoryMock() {

        processingFeeRepository = mock(ProcessingFeeRepository.class);

        ProcessingFee processingFee = new ProcessingFee();
        processingFee.setFee(new BigDecimal("1.98"));
        processingFee.setProductType("T-Shirt");

        doReturn(Optional.of(processingFee)).when(processingFeeRepository).findById("T-Shirt");

    }

    private void setUpTaxRepositoryMock() {
        taxRepository = mock(TaxRepository.class);

        Tax taxNC = new Tax();
        taxNC.setRate(new BigDecimal(".05"));
        taxNC.setState("NC");

        Tax taxNY = new Tax();
        taxNY.setRate(BigDecimal.ZERO);
        taxNY.setState("NY");

        doReturn(Optional.of(taxNC)).when(taxRepository).findById("NC");
        doReturn(Optional.of(taxNY)).when(taxRepository).findById("NY");

    }


}