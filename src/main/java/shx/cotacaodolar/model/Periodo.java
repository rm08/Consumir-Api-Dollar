package shx.cotacaodolar.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Periodo {
    private String dataInicial;
    private String dataFinal;
    private Long diasEntreAsDatas;

    public Periodo(String dataInicial, String dataFinal) throws ParseException{
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
    }

    public String getDiasEntreAsDatasMaisUm(){
        Long resp = diasEntreAsDatas;
        resp =  TimeUnit.DAYS.convert(resp,TimeUnit.MILLISECONDS) +1;
        return resp.toString();
    }

    public String getDataInicial(){
        return this.dataInicial;
    }

    public String getDataFinal(){
        return this.dataFinal;
    }

}
