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
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import ome.xml.model.primitives.NonNegativeInteger;

/**
 * @author Hadrien Mary
 */
public class TiffDataModel extends GenericModel<GenericModel<?>> {

	private final int imageID;
	private final int tiffDataID;
	private final NonNegativeInteger c;
	private final NonNegativeInteger z;
	private final NonNegativeInteger t;
	private final NonNegativeInteger ifd;
	private final String filename;
	private final String uuid;

	private float dt;
	private float exposureTime;
	private float posX;
	private float posY;
	private float posZ;

	private final ImageModel imageModel;

	public TiffDataModel(int imageID, int tiffDataID, OMEXMLMetadata md,
		ImageModel imageModel)
	{
		this.imageID = imageID;
		this.tiffDataID = tiffDataID;
		this.c = md.getTiffDataFirstC(imageID, tiffDataID);
		this.z = md.getTiffDataFirstZ(imageID, tiffDataID);
		this.t = md.getTiffDataFirstT(imageID, tiffDataID);
		this.ifd = md.getTiffDataIFD(imageID, tiffDataID);
		this.filename = md.getUUIDFileName(imageID, tiffDataID);
		this.uuid = md.getUUIDValue(imageID, tiffDataID);

		this.dt = -1;
		this.exposureTime = -1;
		this.posX = -1;
		this.posY = -1;
		this.posZ = -1;

		if (tiffDataID < md.getPlaneCount(imageID)) {
			if (md.getPlaneDeltaT(imageID, tiffDataID) != null) {
				this.dt = md.getPlaneDeltaT(imageID, tiffDataID).value().floatValue();
			}
			if (md.getPlaneExposureTime(imageID, tiffDataID) != null) {
				this.exposureTime = md.getPlaneExposureTime(imageID, tiffDataID).value()
					.floatValue();
			}
			if (md.getPlanePositionX(imageID, tiffDataID) != null) {
				this.posX = md.getPlanePositionX(imageID, tiffDataID).value()
					.floatValue();
			}
			if (md.getPlanePositionY(imageID, tiffDataID) != null) {
				this.posY = md.getPlanePositionY(imageID, tiffDataID).value()
					.floatValue();
			}
			if (md.getPlanePositionZ(imageID, tiffDataID) != null) {
				this.posZ = md.getPlanePositionZ(imageID, tiffDataID).value()
					.floatValue();
			}
		}

		this.imageModel = imageModel;
	}

	@Override
	public String toString() {
		return "TiffData : C = " + c + " | Z = " + z + " | T = " + t;
	}

	public ImageModel getImageModel() {
		return imageModel;
	}

	@Override
	public Iterable<List<String>> getInformationsRow() {
		List<List<String>> rows = new ArrayList<>();

		rows.add(Arrays.asList("TiffData ID", Integer.toString(this.tiffDataID)));
		rows.add(Arrays.asList("IFD", this.ifd.toString()));

		rows.add(Arrays.asList("C position", this.c.toString()));
		rows.add(Arrays.asList("Z position", this.z.toString()));
		rows.add(Arrays.asList("T position", this.t.toString()));

		if (this.dt >= 0) {
			rows.add(Arrays.asList("dt", this.dt + " ms"));
		}
		else {
			rows.add(Arrays.asList("dt", ""));
		}

		if (this.exposureTime >= 0) {
			rows.add(Arrays.asList("Exposure time", this.exposureTime + " ms"));
		}
		else {
			rows.add(Arrays.asList("Exposure time", ""));
		}

		if (this.posX >= 0) {
			rows.add(Arrays.asList("Position X", this.posX + " µm"));
		}
		else {
			rows.add(Arrays.asList("Position X", ""));
		}

		if (this.posY >= 0) {
			rows.add(Arrays.asList("Position Y", this.posY + " µm"));
		}
		else {
			rows.add(Arrays.asList("Position Y", ""));
		}

		if (this.posZ >= 0) {
			rows.add(Arrays.asList("Position Z", this.posZ + " µm"));
		}
		else {
			rows.add(Arrays.asList("Position Z", ""));
		}

		rows.add(Arrays.asList("Filename", this.filename));
		rows.add(Arrays.asList("UUID", this.uuid));

		return rows;
	}

	public Map<AxisType, Long> getPosition() {
		Map<AxisType, Long> positions = new HashMap<>();
		positions.put(Axes.CHANNEL, (Long) this.c.getNumberValue().longValue());
		positions.put(Axes.TIME, (Long) this.t.getNumberValue().longValue());
		positions.put(Axes.Z, (Long) this.z.getNumberValue().longValue());
		return positions;
	}

}
