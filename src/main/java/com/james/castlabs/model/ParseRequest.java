package com.james.castlabs.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParseRequest {
	
	@Schema(description = "url of the MP4 file to parse", example ="https://demo.castlabs.com/tmp/text0.mp4")
	@NotNull(message = "url is requied")
	private String url;
}
