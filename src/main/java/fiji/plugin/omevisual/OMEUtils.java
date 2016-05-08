/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fiji.plugin.omevisual;

import ij.ImagePlus;
import io.scif.Metadata;
import io.scif.filters.AbstractMetadataWrapper;
import io.scif.img.SCIFIOImgPlus;
import io.scif.ome.formats.OMETIFFFormat;
import java.util.Map;
import loci.formats.ome.OMEXMLMetadata;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imagej.display.ImageDisplay;
import org.scijava.convert.ConvertService;
import org.scijava.ui.DialogPrompt;

/**
 *
 * @author Hadrien Mary
 */
public class OMEUtils {

    public static OMEXMLMetadata getOMEXMLMetadata(Dataset data, ImageJ ij) {

        ImgPlus imp = data.getImgPlus();

        if (!(imp instanceof SCIFIOImgPlus)) {
            ij.ui().showDialog("This image has not been opened with SCIFIO.",
                    DialogPrompt.MessageType.ERROR_MESSAGE);
            return null;
        }

        SCIFIOImgPlus sciImp = (SCIFIOImgPlus) imp;
        Metadata metadata = sciImp.getMetadata();

        // Why the fuck this is needed ?
        while ((metadata instanceof AbstractMetadataWrapper)) {
            metadata = ((AbstractMetadataWrapper) metadata).unwrap();
        }

        // Check if metadata are OME or not
        if (!(metadata instanceof OMETIFFFormat.Metadata)) {
            ij.ui().showDialog("This file does not contain OME metadata",
                    DialogPrompt.MessageType.ERROR_MESSAGE);
            return null;
        }

        OMETIFFFormat.Metadata omeMeta = ((OMETIFFFormat.Metadata) metadata);
        OMEXMLMetadata ome = omeMeta.getOmeMeta().getRoot();

        return ome;
    }

    public static void setPosition(ImageDisplay image, Map<AxisType, Long> positions,
            ConvertService convert) {

        // Work with IJ2 interface
        positions.forEach((axis, position) -> {
            image.setPosition(position, axis);
        });

        // Hack to make it work with IJ1
        //ImagePlus imp = convert.convert((Dataset) image.getActiveView().getData(), ImagePlus.class);
        ImagePlus imp = ij.IJ.getImage();

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
