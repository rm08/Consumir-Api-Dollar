package shx.cotacaodolar.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shx.cotacaodolar.model.Moeda;
import shx.cotacaodolar.model.DTO.ConsultaDTO;
import shx.cotacaodolar.service.MoedaService;


@RestController
@RequestMapping(value = "/")
public class MoedaController {

    @Autowired
    private MoedaService moedaService;

    @GetMapping("/moeda")
    public Number currentValue() throws IOException, MalformedURLException, ParseException{
       return moedaService.currentValue();
    }

    @GetMapping("/moeda/hoje")
    public String currentValueAndDate() throws IOException, MalformedURLException{
       return moedaService.currentValueAndDate();
    }

    @GetMapping("/moeda/{data1}&{data2}")
    public ConsultaDTO valuesInTimePeriod(@PathVariable("data1") String startDate, @PathVariable("data2") String endDate) throws IOException, MalformedURLException, ParseException{
        return moedaService.valuesInTimePeriod(startDate, endDate);
    }

    @PostMapping("/moeda/save/{data}")
    public Moeda save(@RequestBody @Valid Moeda moeda, @PathVariable("data") String data){
        return moedaService.save(moeda);
    }
    

}
