/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
