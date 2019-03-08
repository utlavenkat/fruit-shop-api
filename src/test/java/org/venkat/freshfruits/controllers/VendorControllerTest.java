package org.venkat.freshfruits.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.venkat.freshfruits.dto.VendorDTO;
import org.venkat.freshfruits.dto.VendorsListDataDTO;
import org.venkat.freshfruits.exceptions.NotFoundException;
import org.venkat.freshfruits.services.VendorService;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VendorControllerTest {

    private static final String VENDORS_BASE_URL = "/shop/vendors";
    private static final String VENDOR_NAME = "First Name";
    private static final Long VENDOR_ID = 100L;

    @Mock
    private VendorService vendorService;

    private MockMvc mockMvc;
    private VendorController vendorController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        vendorController = new VendorController(vendorService);
        mockMvc = MockMvcBuilders.standaloneSetup(vendorController).setControllerAdvice(new ExceptionControllerAdvise()).build();
    }

    @Test
    public void getAllVendors() throws Exception {
        //Given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(VENDOR_NAME);
        vendorDTO.setVendorUrl(VENDORS_BASE_URL + "/" + VENDOR_ID);

        when(vendorService.findAllVendors()).thenReturn(new VendorsListDataDTO(Collections.singletonList(vendorDTO)));

        //when
        final ResultActions resultActions = mockMvc.perform(get(VENDORS_BASE_URL));

        //then
        resultActions.andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        VendorsListDataDTO vendorsListDataDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), VendorsListDataDTO.class);

        assertNotNull("Response is null", vendorsListDataDTO);
        assertEquals("Incorrect results returns", 1, vendorsListDataDTO.getVendors().size());
    }

    @Test
    public void getVendorById() throws Exception {
        //Given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(VENDOR_NAME);
        when(vendorService.findById(anyLong())).thenReturn(vendorDTO);

        //when
        ResultActions resultActions = mockMvc.perform(get(VENDORS_BASE_URL + "/1"));

        //then
        resultActions.andExpect(status().isOk());
        ObjectMapper objectMapper = new ObjectMapper();

        VendorDTO responseVendorDTO = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString()
                , VendorDTO.class);
        assertNotNull("Vendor is null", responseVendorDTO);
    }

    @Test
    public void getVendorById_InvalidId() throws Exception {
        //Given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(VENDOR_NAME);
        when(vendorService.findById(anyLong())).thenThrow(new NotFoundException("No Vendor Found for the given id"));

        //when
        ResultActions resultActions = mockMvc.perform(get(VENDORS_BASE_URL + "/1"));

        //then
        //then
        resultActions.andExpect(status().isNotFound());

    }

    @Test
    public void createVendor() throws Exception {
        //Given
        VendorDTO vendor1 = new VendorDTO();
        vendor1.setName(VENDOR_NAME);
        ObjectMapper objectMapper = new ObjectMapper();

        when(vendorService.save(any(VendorDTO.class))).thenAnswer(invocationOnMock -> {
            VendorDTO vendorDTO = invocationOnMock.getArgument(0);
            vendorDTO.setVendorUrl(VENDORS_BASE_URL + "/" + VENDOR_ID);
            return vendorDTO;
        });

        //when
        final ResultActions resultActions = mockMvc.perform(post(VENDORS_BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vendor1)));

        //then
        resultActions.andExpect(status().isCreated());
        String response = resultActions.andReturn().getResponse().getContentAsString();
        VendorDTO savedVendor = objectMapper.readValue(response, VendorDTO.class);
        assertNotNull("Vendor is null", savedVendor);
        assertEquals(" Vendor Name is not matching", vendor1.getName(), savedVendor.getName());
        assertNotNull("Vendor URL is null", savedVendor.getVendorUrl());
    }

    @Test
    public void updateVendor() throws Exception {
        final String newName = "New First Name";
        //Given
        VendorDTO vendor1 = new VendorDTO();
        vendor1.setName(newName);
        ObjectMapper objectMapper = new ObjectMapper();

        when(vendorService.updateVendor(anyLong(), any(VendorDTO.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(1));

        //when
        final ResultActions resultActions = mockMvc.perform(patch(VENDORS_BASE_URL + "/" + VENDOR_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vendor1)));

        //then
        resultActions.andExpect(status().isOk());
        String response = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("Response received::" + response);
        VendorDTO vendorDTO = objectMapper.readValue(response, VendorDTO.class);
        assertNotNull("Vendor is null", vendorDTO);
        assertEquals("Vendor name is not matching", newName, vendorDTO.getName());
    }

    @Test
    public void replaceVendor() throws Exception {
        //Given
        VendorDTO vendor1 = new VendorDTO();
        vendor1.setName(VENDOR_NAME);
        ObjectMapper objectMapper = new ObjectMapper();

        when(vendorService.updateVendor(anyLong(), any(VendorDTO.class))).thenReturn(vendor1);

        //when
        final ResultActions resultActions = mockMvc.perform(patch(VENDORS_BASE_URL + "/" + VENDOR_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vendor1)));

        //then
        resultActions.andExpect(status().isOk());
        String response = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("Response received::" + response);
        VendorDTO savedVendor = objectMapper.readValue(response, VendorDTO.class);
        assertNotNull("Vendor is null", savedVendor);
        assertEquals("Vendor Name is not matching", VENDOR_NAME, savedVendor.getName());
    }

    @Test
    public void deleteVendor() throws Exception {
        mockMvc.perform(get(VENDORS_BASE_URL + "/delete/1")).andExpect(status().isOk());
    }

    @Test
    public void deleteVendor_InValidId() throws Exception {
        doThrow(new NotFoundException("No Vendor Found for given id")).when(vendorService).deleteById(anyLong());
        mockMvc.perform(get(VENDORS_BASE_URL + "/delete/1")).andExpect(status().isNotFound());
    }
}