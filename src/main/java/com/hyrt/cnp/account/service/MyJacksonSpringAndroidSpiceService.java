package com.hyrt.cnp.account.service;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by yepeng on 13-12-27.
 */
public class MyJacksonSpringAndroidSpiceService extends JacksonSpringAndroidSpiceService {


    private  MappingJacksonHttpMessageConverter jsonConverter;

    public void setJsonConverter(MappingJacksonHttpMessageConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    public MappingJacksonHttpMessageConverter getJsonConverter() {
        return jsonConverter;
    }

    @Override
    public RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // web services support json responses
        if(jsonConverter == null)
            jsonConverter = new MappingJacksonHttpMessageConverter();
        final List<HttpMessageConverter<?>> listHttpMessageConverters = restTemplate.getMessageConverters();

        listHttpMessageConverters.add(jsonConverter);
        restTemplate.setMessageConverters(listHttpMessageConverters);
        return restTemplate;
    }


}
