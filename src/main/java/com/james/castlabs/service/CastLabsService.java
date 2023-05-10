package com.james.castlabs.service;

import org.springframework.stereotype.Service;
import com.james.castlabs.model.MP4Box;
import com.james.castlabs.util.Constants;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.InputStream;

/**
 *  The {@code CastLabsService} class retrieves an MP4 file
 *  from a URL, extracts the MP4 boxes from it creating
 *  a nested list of MP4Box objects and returning this
 *  to the caller.
 */
@Service
@RequiredArgsConstructor
public class CastLabsService {
	
	/**
     * The input url is retrieved via a buffered stream and passed to a
     * data stream for processing.  This approach ensures that the file
     * is processed directly as received and is not stored in memory.
     */
    public List<MP4Box> readMP4FromUri(String uri) throws IOException, InterruptedException, MalformedURLException {
    	URL url = new URL(uri);
    	URLConnection urlConnection = url.openConnection();
    	
        InputStream bin = new BufferedInputStream(urlConnection.getInputStream());
        DataInputStream in = new DataInputStream(bin);
    	
    	List<MP4Box> boxes = processFile(in);
    	in.close();
    	return boxes;
    }
    
    /**
     * The processing of extracting MP4 boxes begins with reading the first four
     * bytes in the file which specify the length of the first box.  This is 
     * followed by reading the next four bytes which specify the box type.  Once
     * the first box is read all child boxes are read recursively and attached
     * to the parent box.  In case a child box is a MOOF or TRAF container its
     * children are associated with it instead of the root parent.  The process
     * repeats if there's additional data after the end of parent.  This produces
     * the nested MP4 box structure which is then returned.
     */
    public List<MP4Box> processFile(DataInputStream in) throws IOException{
    	List<MP4Box> boxes = new ArrayList<>();
    	boolean more = true;
    	while (more) {
    		try {
		    	int totalLength = in.readInt();
		    	byte[] boxTypeBytes = new byte[4];
			    in.readFully(boxTypeBytes);
			    String type = new String(boxTypeBytes, StandardCharsets.UTF_8);
			    MP4Box parent = MP4Box.builder().length(totalLength).type(type).build();
			    totalLength -= 8;
			    
			    if (type.equals(Constants.ParentBoxType.moof.name()) || type.equals(Constants.ParentBoxType.traf.name()))
			    	getNextBox(in, parent, totalLength);
			    else 
			    	in.skipBytes(totalLength);
			    
			    boxes.add(parent);
    		} catch (EOFException e) {
    			more = false;
    		} catch (IOException e) {
    			more = false;
    		}
    	}
    	return boxes;
    }
    
    private void getNextBox(DataInputStream in, MP4Box parent, int totalLength) throws IOException {
    	if (totalLength > 0) {
	    	int nextLength = in.readInt();
	    	if (nextLength <= totalLength) {
		    	String nextType = getBoxType(in);
		    	totalLength -= 8;
		    	
		    	MP4Box newBox = MP4Box.builder().length(nextLength).type(nextType).build();
		    	parent.getBoxes().add(newBox);
		    	if (nextType.equals(Constants.ParentBoxType.moof.name()) || nextType.equals(Constants.ParentBoxType.traf.name())) {
		    		getNextBox(in, newBox, totalLength);
		    	}
		    	else {
		    		int skipLength = nextLength - 8;
		    		in.skipBytes(skipLength);
		    		totalLength -= skipLength;
		        	getNextBox(in, parent, totalLength);
		    	}
	    	}
    	}
    }
    
    private String getBoxType(DataInputStream in) throws IOException {
    	byte[] bytes = new byte[4];
    	in.readFully(bytes);
    	return new String(bytes, StandardCharsets.UTF_8);
    }
    
}
