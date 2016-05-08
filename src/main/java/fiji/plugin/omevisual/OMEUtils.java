/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fiji.plugin.omevisual;

import io.scif.Metadata;
import io.scif.filters.AbstractMetadataWrapper;
import io.scif.img.SCIFIOImgPlus;
import io.scif.ome.OMEMetadata;
import io.scif.ome.formats.OMETIFFFormat;
import loci.formats.ome.OMEXMLMetadata;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.ImgPlus;
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

}
