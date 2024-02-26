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
import java.util.Objects;
import java.util.stream.Stream;

public class ViaCepService {

    private Endereco endereco = null;
    public String cep;

    public ViaCepService(String cep) {
        this.cep = cep;
        Endereco endereco = buscar(cep);

        System.out.println(endereco);
    }

    public ViaCepService() {}

    public Endereco buscar(String cep) {
        HttpGet request = new HttpGet("https://viacep.com.br/ws/"+cep+"/json/");

        try (
                CloseableHttpClient httpClient = HttpClientBuilder.create()
                        .disableRedirectHandling()
                        .build();
                CloseableHttpResponse response = httpClient.execute(request)
            )
        {
            HttpEntity entity = response.getEntity();
            if (entity == null) {return endereco;}

            String result = getEntityString(entity);
            this.endereco = convertToEndereco(result);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this.endereco;
    }
   public Endereco buscar() {
        HttpGet request = new HttpGet("https://viacep.com.br/ws/"+this.cep+"/json/");

        try (
                CloseableHttpClient httpClient = HttpClientBuilder.create()
                        .disableRedirectHandling()
                        .build();
                CloseableHttpResponse response = httpClient.execute(request)
        )
        {
            HttpEntity entity = response.getEntity();
            if (entity == null) {return endereco;}

            String result = getEntityString(entity);
            this.endereco = convertToEndereco(result);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this.endereco;
    }
    private static String getEntityString(HttpEntity entity) {
        String entityString;
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

    public boolean cepExists() {
        boolean cepExist = Stream.of(endereco.getCep(), endereco.getUf())
                .allMatch(Objects:: isNull);

        return !cepExist;
    }

    public Endereco getEndereco() {
        return this.endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
