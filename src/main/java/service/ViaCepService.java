package service;

import com.google.gson.Gson;
import model.Endereco;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.net.http.HttpClient;

public class ViaCepService {


    public Endereco getEndereco(String cep) {
        Endereco endereco = null;
        HttpGet request = new HttpGet("https://viacep.com.br/ws/"+cep+"/json/");

        try (
                CloseableHttpClient httpClient = HttpClientBuilder.create()
                        .disableRedirectHandling()
                        .build();
                CloseableHttpResponse response = httpClient.execute(request);
            )
        {
            HttpEntity entity = response.getEntity();
            if (entity == null) {return endereco;}

            String result = getEntityString(entity);
            endereco = convertToEndereco(result);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return endereco;
    }

    private static String getEntityString(HttpEntity entity) {
        String entityString = null;
        try {
            entityString = EntityUtils.toString(entity);
        }
        catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        return entityString;
    }

    private static Endereco convertToEndereco(String responseResult) {
        Endereco endereco = null;
        Gson gson = new Gson();

        endereco = gson.fromJson(responseResult, Endereco.class);
        return endereco;

    }
}
