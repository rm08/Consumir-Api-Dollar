package shx.cotacaodolar.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import shx.cotacaodolar.model.Moeda;
import shx.cotacaodolar.model.Periodo;
import shx.cotacaodolar.model.DTO.ConsultaDTO;
import shx.cotacaodolar.repository.MoedaRepository;

@Service
public class MoedaService {

	@Autowired
	private MoedaRepository moedaRepository;

	@JsonIgnoreProperties
	public Number currentValue() throws IOException, MalformedURLException, ParseException {
		ConsultaDTO consulta = consultaCotacao();
		if(consulta.getValue().isEmpty()) {
			return 0;
		}

		String[] dateAndHour = consulta.getValue().get(0).getDataHoraCotacao().split(" ");
		Moeda moeda = new Moeda(consulta.getValue().get(0).getCotacaoCompra(), dateAndHour[0], dateAndHour[1]);	
		if (!exists(moeda.getData()))save(moeda);
		return consulta.getValue().get(0).getCotacaoCompra();
	}

	public String currentValueAndDate() throws IOException, MalformedURLException {
		ConsultaDTO consulta = consultaCotacao();
		if(consulta.getValue().isEmpty()) {
			return "";
		}
		
		String[] dateAndHour = consulta.getValue().get(0).getDataHoraCotacao().split(" ");
		Moeda moeda = new Moeda(consulta.getValue().get(0).getCotacaoCompra(), dateAndHour[0], dateAndHour[1]);	
		
		Moeda searchMoeda = findByData(moeda.getData());
		
		if(searchMoeda != null) {
			searchMoeda.setPreco(moeda.getPreco());
			searchMoeda.setHora(moeda.getHora());
			save(searchMoeda);
		} else {
			save(moeda);
		}
		
		return consulta.getValue().get(0).getCotacaoCompra() + " " + consulta.getValue().get(0).getDataHoraCotacao();
	}

	public ConsultaDTO valuesInTimePeriod(String startDate, String endDate)
			throws IOException, MalformedURLException, ParseException {
		List<Moeda> moedas = new ArrayList<>();
		Periodo periodo = new Periodo(startDate, endDate);

		ConsultaDTO consulta = consultaCotacaoPeriodo(periodo);

		for(int i=0; i<consulta.getValue().size(); i++) {
	        
	        String[] dateAndHour = consulta.value.get(i).getDataHoraCotacao().split(" ");
			
			Moeda moeda = new Moeda(consulta.value.get(i).getCotacaoCompra(), dateAndHour[0], dateAndHour[1]);
			
			if (!exists(moeda.getData())) moedas.add(moeda);
			
		}
		saveAll(moedas);
		return consulta;
	}
	
	public boolean exists(String data) {
		return moedaRepository.existsByData(data);
	}
	
	public List<Moeda> saveAll(List<Moeda> moedas) {
		return moedaRepository.saveAll(moedas);
	}

	public Moeda save(Moeda moeda) {
		return moedaRepository.save(moeda);
	}
	
	public Moeda findByData(String data) {
		return moedaRepository.findByData(data);
	}
	
	Date date = new Date();
	SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

	public ConsultaDTO consultaCotacao() throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("application/json");
		Request request = new Request.Builder()
				.url("https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/CotacaoDolarDia(dataCotacao=@dataCotacao)?@dataCotacao='" + formatter.format(date) +"'&$top=100&$format=json&$select=cotacaoCompra,dataHoraCotacao")
				.method("GET", null)
				.addHeader("Content-Type", "application/json")
				.addHeader("Cookie", "TS01d9825e=0198c2d644c9ab0f79d5cbb3928dc709ac965040dda65db974c943095e681420799caf328a359c4f12c8868fbbd9e143b35c92e21f; dtCookie=C9953E3209821AF2B9FD6EC29D3866F6|cHRheHwx; BIGipServer~was-p_as3~was-p~pool_was-p_default_443=4275048876.47873.0000; JSESSIONID=00000Q8xOavgxKXy2Ynp5UgIcb_:1dof89mke; TS013694c2=0198c2d644f1d8ad32dd50d6b6b6863f129a8dd240baf2d1836bec05729d95626ae84cb011e4605be35b4d41c27cb6526cc4e39b9b")
				.build();
		Response response = client.newCall(request).execute();

		return new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false)
				.readValue(response.body()
				.string(), ConsultaDTO.class);
	}

	public ConsultaDTO consultaCotacaoPeriodo(Periodo periodo) throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder().url("https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/CotacaoDolarPeriodo(dataInicial=@dataInicial,dataFinalCotacao=@dataFinalCotacao)?@dataInicial='" + periodo.getDataInicial() + "'&@dataFinalCotacao='" + periodo.getDataFinal() + "'&$top=100&$format=json&$select=cotacaoCompra,dataHoraCotacao")
				.method("GET", null)
				.addHeader("Content-Type", "application/json")
				.addHeader("Cookie",
						"TS01d9825e=0198c2d644c9ab0f79d5cbb3928dc709ac965040dda65db974c943095e681420799caf328a359c4f12c8868fbbd9e143b35c92e21f; dtCookie=C9953E3209821AF2B9FD6EC29D3866F6|cHRheHwx; BIGipServer~was-p_as3~was-p~pool_was-p_default_443=4275048876.47873.0000; JSESSIONID=00000Q8xOavgxKXy2Ynp5UgIcb_:1dof89mke; TS013694c2=0198c2d644f1d8ad32dd50d6b6b6863f129a8dd240baf2d1836bec05729d95626ae84cb011e4605be35b4d41c27cb6526cc4e39b9b")
				.build();
		Response response = client.newCall(request).execute();

		return new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.readValue(response.body()
				.string(), ConsultaDTO.class);
	}

}
