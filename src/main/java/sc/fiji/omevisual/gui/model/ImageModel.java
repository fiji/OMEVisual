/*-
 * #%L
 * A Fiji plugin to easily visualize OME metadata.
 * %%
 * Copyright (C) 2016 - 2025 Fiji developers.
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

package sc.fiji.omevisual.gui.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loci.formats.ome.OMEXMLMetadata;
import ome.xml.model.primitives.PositiveInteger;

/**
 * @author Hadrien Mary
 */
public class ImageModel extends GenericModel<TiffDataModel> {

	private final int imageID;
	private final String id;
	private final String name;
	private final String pixelID;
	private final float physicalSizeX;
	private final float physicalSizeY;
	private final float physicalSizeZ;
	private final float timeIncrement;
	private final PositiveInteger sizeC;
	private final PositiveInteger sizeT;
	private final PositiveInteger sizeX;
	private final PositiveInteger sizeY;
	private final PositiveInteger sizeZ;

	private final List<Map<String, String>> channels;

	public ImageModel(int imageID, OMEXMLMetadata md) {
		this.imageID = imageID;
		this.id = md.getImageID(imageID);
		this.name = md.getImageName(imageID);

		this.pixelID = md.getPixelsID(imageID);

		if (md.getPixelsPhysicalSizeX(imageID) != null) {
			this.physicalSizeX = md.getPixelsPhysicalSizeX(imageID).value()
				.floatValue();
		}
		else {
			this.physicalSizeX = -1;
		}

		if (md.getPixelsPhysicalSizeY(imageID) != null) {
			this.physicalSizeY = md.getPixelsPhysicalSizeY(imageID).value()
				.floatValue();
		}
		else {
			this.physicalSizeY = -1;
		}

		if (md.getPixelsPhysicalSizeZ(imageID) != null) {
			this.physicalSizeZ = md.getPixelsPhysicalSizeZ(imageID).value()
				.floatValue();
		}
		else {
			this.physicalSizeZ = -1;
		}

		if (md.getPixelsTimeIncrement(imageID) != null) {
			this.timeIncrement = md.getPixelsTimeIncrement(imageID).value()
				.floatValue();
		}
		else {
			this.timeIncrement = -1;
		}

		this.sizeC = md.getPixelsSizeC(imageID);
		this.sizeT = md.getPixelsSizeT(imageID);
		this.sizeX = md.getPixelsSizeX(imageID);
		this.sizeY = md.getPixelsSizeY(imageID);
		this.sizeZ = md.getPixelsSizeZ(imageID);

		channels = new ArrayList<>();
		for (int i = 0; i < md.getChannelCount(imageID); i++) {
			Map<String, String> channel = new HashMap<>();
			channel.put("ID", md.getChannelID(imageID, i));
			channel.put("Name", md.getChannelName(imageID, i));
			channels.add(channel);
		}

	}

	@Override
	public String toString() {
		return "Image : " + name;
	}

	@Override
	public Iterable<List<String>> getInformationsRow() {
		List<List<String>> rows = new ArrayList<>();

		rows.add(Arrays.asList("Name", this.name));
		rows.add(Arrays.asList("ID", this.id));
		rows.add(Arrays.asList("Pixel ID", this.pixelID));

		if (this.physicalSizeX > -1) {
			rows.add(Arrays.asList("Physical Size X", this.physicalSizeX + " µm"));
		}
		else {
			rows.add(Arrays.asList("Physical Size X", ""));
		}

		if (this.physicalSizeY > -1) {
			rows.add(Arrays.asList("Physical Size Y", this.physicalSizeY + " µm"));
		}
		else {
			rows.add(Arrays.asList("Physical Size Y", ""));
		}

		if (this.physicalSizeZ > -1) {
			rows.add(Arrays.asList("Physical Size Z", this.physicalSizeZ + " µm"));
		}
		else {
			rows.add(Arrays.asList("Physical Size Z", ""));
		}

		if (this.timeIncrement > -1) {
			rows.add(Arrays.asList("Time Increment", this.timeIncrement + " s"));
		}
		else {
			rows.add(Arrays.asList("Time Increment", ""));
		}

		rows.add(Arrays.asList("Size X", this.sizeX.toString()));
		rows.add(Arrays.asList("Size Y", this.sizeY.toString()));
		rows.add(Arrays.asList("Size Z", this.sizeZ.toString()));
		rows.add(Arrays.asList("Size Channel", this.sizeC.toString()));
		rows.add(Arrays.asList("Size Time", this.sizeT.toString()));

		for (int i = 0; i < channels.size(); i++) {
			Map<String, String> channel = channels.get(i);
			for (Map.Entry<String, String> entry : channel.entrySet()) {
				rows.add(Arrays.asList("Channel " + Integer.toString(i) + " - " + entry
					.getKey(), entry.getValue()));
			}
		}

		return rows;
	}
}
