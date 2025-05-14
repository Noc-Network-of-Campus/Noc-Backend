package com.mycompany.myapp.config;

import static org.apache.http.ssl.SSLContextBuilder.*;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.mycompany.myapp.repository") // 경로 맞게 수정
public class ElasticsearchConfig {

	@Value("${spring.data.elasticsearch.rest.uris}")
	private String elasticsearchUrl;

	@Value("${spring.data.elasticsearch.rest.username}")
	private String username;

	@Value("${spring.data.elasticsearch.rest.password}")
	private String password;

	@Bean
	public RestHighLevelClient restHighLevelClient() throws Exception {
		final SSLContext sslContext = create()
			.loadTrustMaterial((chain, authType) -> true) // 인증서 무시
			.build();

		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,
			new UsernamePasswordCredentials(username, password));

		RestClientBuilder builder = RestClient.builder(HttpHost.create(elasticsearchUrl))
			.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
				.setSSLContext(sslContext)
				.setDefaultCredentialsProvider(credentialsProvider)
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE));

		return new RestHighLevelClient(builder);
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate(RestHighLevelClient client) {
		return new ElasticsearchRestTemplate(client);
	}
}