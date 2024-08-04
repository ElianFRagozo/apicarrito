package com.msvccarritocompras.infrastructure.api.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private static final ThreadLocal<Long> usuarioIdThreadLocal = new ThreadLocal<>();

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String url = requestTemplate.url();
        if (url.contains("/")) {
            String[] parts = url.split("/");
            try {
                Long id = Long.parseLong(parts[parts.length - 1]);
                usuarioIdThreadLocal.set(id);
            } catch (NumberFormatException ignored) {
            }
        }
    }

    public static Long getId() {
        return usuarioIdThreadLocal.get();
    }

    public static void clear() {
        usuarioIdThreadLocal.remove();
    }
}
