/*-
 * #%L
 * A Fiji plugin to easily visualize OME metadata.
 * %%
 * Copyright (C) 2016 - 2020 Fiji developers.
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

package sc.fiji.omevisual;

import io.scif.Metadata;
import io.scif.img.SCIFIOImgPlus;
import io.scif.ome.OMEMetadata;
import io.scif.services.TranslatorService;

import java.util.Map;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imagej.display.ImageDisplay;

import org.scijava.convert.ConvertService;
import org.scijava.ui.DialogPrompt;

import ij.ImagePlus;
import loci.formats.ome.OMEXMLMetadata;

/**
 * @author Hadrien Mary
 */
public class OMEUtils {

	public static OMEXMLMetadata getOMEXMLMetadata(Dataset data, ImageJ ij,
		TranslatorService translatorService)
	{

		ImgPlus<?> imp = data.getImgPlus();

		if (!(imp instanceof SCIFIOImgPlus)) {
			ij.ui().showDialog("This image has not been opened with SCIFIO.",
				DialogPrompt.MessageType.ERROR_MESSAGE);
			return null;
		}

		SCIFIOImgPlus<?> sciImp = (SCIFIOImgPlus<?>) imp;
		Metadata metadata = sciImp.getMetadata();
		OMEMetadata omeMeta = new OMEMetadata(translatorService.getContext());

		if (!translatorService.translate(metadata, omeMeta, true)) {
			ij.ui().showDialog("Unable to extract OME Metadata",
				DialogPrompt.MessageType.ERROR_MESSAGE);
			return null;
		}

		OMEXMLMetadata ome = omeMeta.getRoot();

		return ome;
	}

	public static void setPosition(ImageDisplay image,
		Map<AxisType, Long> positions, ConvertService convert)
	{

		// Work with IJ2 interface
		positions.forEach((axis, position) -> {
			image.setPosition(position, axis);
		});

		ImagePlus imp = convert.convert(image, ImagePlus.class);

		if (positions.get(Axes.Z).intValue() >= 0) {
			imp.setZ(positions.get(Axes.Z).intValue() + 1);
		}
		if (positions.get(Axes.CHANNEL).intValue() >= 0) {
			imp.setC(positions.get(Axes.CHANNEL).intValue() + 1);
		}
		if (positions.get(Axes.TIME).intValue() >= 0) {
			imp.setT(positions.get(Axes.TIME).intValue() + 1);
		}
	}

}
