package com.james.castlabs.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.net.MalformedURLException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.james.castlabs.model.ParseRequest;

@SpringBootTest
public class CastLabsServiceUnitTest {

	@Autowired
	CastLabsService service;
	
	@Test
	public void givenParseRequest_whenUrlIsInvalid_thenValidateMalformedURLExcpetionIsThrown() {
		ParseRequest request = new ParseRequest();
		request.setUrl("httt://demo.castlabs.com/tmp/text0.mp4");
		
		Exception exception = assertThrows(MalformedURLException.class, () -> {
			service.readMP4FromUri(request.getUrl());
		});
		
		String expectMessageStart = "unknown protocol";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.startsWith(expectMessageStart));
	}
	
}
