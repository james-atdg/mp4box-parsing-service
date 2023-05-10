package com.james.castlabs.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import com.james.castlabs.model.MP4Box;
import com.james.castlabs.util.Constants;
import lombok.RequiredArgsConstructor;

import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
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
	
	/**
     * The input url is retrieved via a readable byte channel and written
     * to a temp file where it is then processed and deleted after completion.
     * The file is written to disk first in order to determine the length
     * of the file which is utilized is handling cases where the last
     * box is not a MOOF or TRAF container.
     */
    public List<MP4Box> readMP4FromUri(String uri) throws IOException, InterruptedException, MalformedURLException {
    	URL url = new URL(uri);
    	File file = File.createTempFile("temp", null);
        
    	ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
    	FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath());
    	fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
    	
    	DataInputStream in = new DataInputStream(new FileInputStream(file));
    	
    	List<MP4Box> boxes = processFile(in, file.length());
    	file.delete();
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
    public List<MP4Box> processFile(DataInputStream in, long contentLength) throws IOException{
    	List<MP4Box> boxes = new ArrayList<>();
    	boolean more = true;
    	while (more) {
    		try {
		    	int totalLength = in.readInt();
		    	if (contentLength < totalLength) break; //past the end of file
			    byte[] boxTypeBytes = new byte[4];
			    in.readFully(boxTypeBytes);
			    String type = new String(boxTypeBytes, StandardCharsets.UTF_8);
			    MP4Box parent = MP4Box.builder().length(totalLength).type(type).build();
			    totalLength -= 8;
			    getNextBox(in, parent, totalLength);
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
