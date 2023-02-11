package com.titxu.cloud.wechat.infrastructure.feign.wechat.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.titxu.cloud.common.core.exception.BaseException;
import feign.FeignException;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.EncodeException;
import io.micrometer.core.instrument.util.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 驼峰转下划线
 */
public class FeignClientEncoder extends SpringEncoder {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.disable(JsonGenerator.Feature.IGNORE_UNKNOWN);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
    }

    public FeignClientEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {

        try {
            super.encode(object, bodyType, template);
            template.body(objectMapper.writeValueAsString(object));
        } catch (Exception ex) {
            throw new BaseException("错误");
        }

    }

    public static class FeignClientDecoder extends SpringDecoder {
        public FeignClientDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
            super(messageConverters);
        }

        @Override
        public Object decode(Response response, Type type) throws IOException, FeignException {
            //FIX：Feign默认Body返回的是String,换成ApacheHttpClient以后是一个匿名类
            String result = IOUtils.toString(response.body().asInputStream());
            if (type instanceof Class && StringUtils.isNoneBlank(result)) {
                Response build = Response.builder()
                        .body(new ObjectMapper().writeValueAsString(
                                        objectMapper.readValue(result,
                                                (Class<? extends Object>) type)),
                                StandardCharsets.UTF_8)
                        .headers(response.headers())
                        .reason(response.reason())
                        .status(response.status())
                        .request(response.request())
                        .build();
                //思考：这里是不是可以直接返回，因为使用这个Decoder的FeignClient应该是某个系统专用的。
                //return objectMapper.readValue(result, (Class<? extends Object>) type))；
                return super.decode(build, type);
            }
            return super.decode(response, type);
        }
    }
}
