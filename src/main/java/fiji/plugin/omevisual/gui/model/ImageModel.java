/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fiji.plugin.omevisual.gui.model;

import loci.formats.ome.OMEXMLMetadata;
import ome.units.quantity.Length;
import ome.units.quantity.Time;
import ome.xml.model.primitives.PositiveInteger;

/**
 *
 * @author Hadrien Mary
 */
public class ImageModel extends GenericModel<TiffDataModel> {

    private final int imageID;
    private final String id;
    private final String name;
    private final String pixelID;
    private final Length physicalSizeX;
    private final Length physicalSizeY;
    private final Length physicalSizeZ;
    private final Time timeIncrement;
    private final PositiveInteger sizeC;
    private final PositiveInteger sizeT;
    private final PositiveInteger sizeX;
    private final PositiveInteger sizeY;
    private final PositiveInteger sizeZ;

    public ImageModel(int imageID, OMEXMLMetadata md) {
        this.imageID = imageID;
        this.id = md.getImageID(imageID);
        this.name = md.getImageName(imageID);
        
        this.pixelID = md.getPixelsID(imageID);
        
        this.physicalSizeX = md.getPixelsPhysicalSizeX(imageID);
        this.physicalSizeY = md.getPixelsPhysicalSizeY(imageID);
        this.physicalSizeZ = md.getPixelsPhysicalSizeZ(imageID);
        this.timeIncrement = md.getPixelsTimeIncrement(imageID);
        this.sizeC = md.getPixelsSizeC(imageID);
        this.sizeT = md.getPixelsSizeT(imageID);
        this.sizeX = md.getPixelsSizeX(imageID);
        this.sizeY = md.getPixelsSizeY(imageID);
        this.sizeZ = md.getPixelsSizeZ(imageID);
    }

    @Override
    public String toString() {
        return "Image : " + name;
    }

}
