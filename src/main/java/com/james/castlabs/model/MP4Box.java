package com.james.castlabs.model;

import java.util.List;
import java.util.ArrayList;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MP4Box {
	private int length;
	private String type;
	@Builder.Default
	private List<MP4Box> children = new ArrayList<>();
}
