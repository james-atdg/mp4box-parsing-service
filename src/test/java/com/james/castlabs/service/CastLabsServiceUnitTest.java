package com.james.castlabs.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.james.castlabs.model.MP4Box;
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
	
	@Test
	public void givenParseRequest_whenUrlIsTestFile_thenValidateExpectedResultIsReturned() {
		ParseRequest request = new ParseRequest();
		request.setUrl("https://demo.castlabs.com/tmp/text0.mp4");
		
		/*	Expected Structure
		 * 	MOOF
		 * 		MFHD
		 * 		TRAF
		 * 			TFHD
		 * 			TRUN
		 * 			UUID
		 * 			UUID
		 * 	MDAT
		 */
		
		List<MP4Box> boxes = null;
		
		try {
	        boxes = service.readMP4FromUri(request.getUrl());
	    } catch (Exception e) {
	        fail("Unexpected exception was thrown");
	    }
		
		assertTrue(boxes.get(0).getType().equals("moof"));
		assertTrue(boxes.get(0).getBoxes().get(0).getType().equals("mfhd"));
		assertTrue(boxes.get(0).getBoxes().get(1).getType().equals("traf"));
		assertTrue(boxes.get(0).getBoxes().get(1).getBoxes().get(0).getType().equals("tfhd"));
		assertTrue(boxes.get(0).getBoxes().get(1).getBoxes().get(1).getType().equals("trun"));
		assertTrue(boxes.get(0).getBoxes().get(1).getBoxes().get(2).getType().equals("uuid"));
		assertTrue(boxes.get(0).getBoxes().get(1).getBoxes().get(3).getType().equals("uuid"));
		assertTrue(boxes.get(1).getType().equals("mdat"));
		
	}
	
}
