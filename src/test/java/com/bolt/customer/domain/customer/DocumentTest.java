package com.bolt.customer.domain.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.bolt.customer.domain.exception.BusinessException;

class DocumentTest {

	@Test
	void shouldNormalizeDocument() {
		Document document = Document.of("123.456.789-01");

		assertThat(document.value()).isEqualTo("12345678901");
	}

	@Test
	void shouldRejectBlankDocument() {
		assertThatThrownBy(() -> Document.of(" "))
				.isInstanceOf(BusinessException.class)
				.hasMessage("Document is required");
	}

	@Test
	void shouldRejectDocumentWithoutDigits() {
		assertThatThrownBy(() -> Document.of("abc"))
				.isInstanceOf(BusinessException.class)
				.hasMessage("Document must contain digits");
	}

	@Test
	void shouldCompareDocumentsByNormalizedValue() {
		assertThat(Document.of("123.456.789-01")).isEqualTo(Document.of("12345678901"));
	}
}
