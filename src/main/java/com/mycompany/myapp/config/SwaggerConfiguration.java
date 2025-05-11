package com.mycompany.myapp.config;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.nimbusds.jose.proc.SecurityContext;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2 설정 클래스
 * - API 문서 자동 생성 및 UI 제공
 * - JWT 인증을 위한 Authorization 헤더 설정 포함
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    /**
     * Swagger가 스캔할 기본 패키지
     * (해당 패키지의 컨트롤러를 대상으로 문서화 수행)
     */
    @Value("com.mycompany.myapp.web.controller")
    private String basePackage;

    /**
     * Swagger Docket 빈 등록
     * - API 문서화 대상 및 보안 설정 구성
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(this.basePackage))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(this.apiInfo())
                .useDefaultResponseMessages(false)
                .securitySchemes(Arrays.asList(apiKey()))
                .securityContexts(Arrays.asList(securityContext()));
    }

    /**
     * Swagger UI 상단에 표시할 API 정보 설정
     */
    private ApiInfo apiInfo() {
//        Contact contact = new Contact("Jaeyun Park", "https://github.com/qkrwodsbfjq", "qkrwodbsfjq@khu.ac.kr");

        String title = "Noc API Documents"; // 스웨거 UI 타이틀
        String version = "1.0.0";

        return new ApiInfoBuilder()
                .title(title)
                .version(version)
//                .contact(contact)
                .build();
    }

    /**
     * JWT 토큰을 위한 Authorization 헤더 설정
     */
    private ApiKey apiKey() {
        return new ApiKey("Bearer", "Authorization", "header");
    }

    /**
     * 인증이 필요한 API를 위한 SecurityContext 설정
     */
    // SecurityContext 추가
    private springfox.documentation.spi.service.contexts.SecurityContext securityContext() {
        return springfox.documentation.spi.service.contexts.SecurityContext.builder()
            .securityReferences(defaultAuth())
            .build();
    }

    /**
     * 전역 인증 스코프 설정 (모든 API에서 Authorization 헤더 사용)
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Bearer", authorizationScopes));

    }


}