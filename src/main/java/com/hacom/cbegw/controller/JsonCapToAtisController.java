package com.hacom.cbegw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hacom.cbegw.entity.Alert;
import com.hacom.cbegw.entity.CBEM_CBS_Request;
import com.hacom.cbegw.service.JsonCapToAtisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@RestController
public class JsonCapToAtisController {
	
	@Autowired
    private JsonCapToAtisService jsonCapToAtisService;
	
	@PostMapping(value = "/jsonCapToAtis", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> convertJsonCapToAtis(@RequestBody Object alertJson) {
        try {
        	ObjectMapper objectMapper = new ObjectMapper();
            Alert alert = objectMapper.convertValue(alertJson, Alert.class);
            CBEM_CBS_Request cbemcbsRequest = jsonCapToAtisService.jsonCapToAtis(alert);

            XmlMapper xmlMapper = new XmlMapper();
            String xmlString = xmlMapper.writeValueAsString(cbemcbsRequest);
            
            return ResponseEntity.ok(xmlString);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al convertir JSON a XML");
        }
    }
	
}
