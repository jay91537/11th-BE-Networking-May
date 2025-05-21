package cotato.networking.weather_api.common.config;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import cotato.networking.weather_api.common.property.property.WeatherApiProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class WebClientConfig {

	private final WeatherApiProperties weatherApiProperties;

	@Bean
	public WebClient weatherWebClient() {
		// HTTP 클라이언트 설정
		HttpClient httpClient = HttpClient.create()
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, weatherApiProperties.getConnectTimeout())
			.responseTimeout(Duration.ofMillis(weatherApiProperties.getReadTimeout()))
			.doOnConnected(connection -> connection
				.addHandlerLast(new ReadTimeoutHandler(weatherApiProperties.getReadTimeout(), TimeUnit.MILLISECONDS))
				.addHandlerLast(
					new WriteTimeoutHandler(weatherApiProperties.getWriteTimeout(), TimeUnit.MILLISECONDS)));

		// 메모리 버퍼 크기 설정
		ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
			.codecs(configurer -> configurer.defaultCodecs()
				.maxInMemorySize(16 * 1024 * 1024)) // 16MB
			.build();

		// WebClient 구성
		return WebClient.builder()
			.baseUrl(weatherApiProperties.getBaseUrl())
			.clientConnector(new ReactorClientHttpConnector(httpClient))
			.exchangeStrategies(exchangeStrategies)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();
	}
}