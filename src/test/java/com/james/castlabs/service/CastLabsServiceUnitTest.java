package com.james.castlabs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.james.castlabs.service.CastLabsService;

@SpringBootTest
public class CastLabsServiceUnitTest {

	private Logger logger = LoggerFactory.getLogger(CastLabsServiceUnitTest.class);
	
	@Autowired
	CastLabsService service;
	
	@Test
	public void givenADictionaryOfWords_whenDictionaryIsIndexed_thenValidateAllWordsArePresentInIndex() {
		
		assertEquals(0, 0);
	}
	
}
