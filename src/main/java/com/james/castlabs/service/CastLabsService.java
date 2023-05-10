package com.james.castlabs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.james.castlabs.model.MP4Box;
import com.james.castlabs.util.Constants;
import lombok.RequiredArgsConstructor;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;

/**
 *  The {@code CastLabsService} class ...
 *  <p>
 *  ...
 *  </p>
 */
@Service
@RequiredArgsConstructor
public class CastLabsService {

	private Logger logger = LoggerFactory.getLogger(CastLabsService.class);
	
//	@Value("classpath:media/text0.mp4")
//	Resource resourceFile;
//	
//    @PostConstruct
//    private void initialize() throws IOException {
//    	//InputStream resource = resourceFile.getInputStream();
//    	
//    }
    
	
    
}
