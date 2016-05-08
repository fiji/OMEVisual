/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fiji.plugin.omevisual.gui.model;

import loci.formats.ome.OMEXMLMetadata;
import ome.units.quantity.Length;
import ome.units.quantity.Time;
import ome.xml.model.primitives.NonNegativeInteger;

/**
 *
 * @author Hadrien Mary
 */
public class TiffDataModel extends GenericModel<GenericModel<?>> {

    private final int imageID;
    private final int tiffDataID;
    private final NonNegativeInteger c;
    private final NonNegativeInteger z;
    private final NonNegativeInteger t;
    private final NonNegativeInteger ifd;
    private final Time dt;
    private final Time exposureTime;
    private final Length posX;
    private final Length posY;
    private final Length posZ;
    private final ImageModel imageModel;

    public TiffDataModel(int imageID, int tiffDataID, OMEXMLMetadata md, ImageModel imageModel) {
        this.imageID = imageID;
        this.tiffDataID = tiffDataID;
        this.c = md.getTiffDataFirstC(imageID, tiffDataID);
        this.z = md.getTiffDataFirstZ(imageID, tiffDataID);
        this.t = md.getTiffDataFirstT(imageID, tiffDataID);
        this.ifd = md.getTiffDataIFD(imageID, tiffDataID);
        
        this.dt = md.getPlaneDeltaT(imageID, tiffDataID);
        this.exposureTime = md.getPlaneExposureTime(imageID, tiffDataID);
        this.posX = md.getPlanePositionX(imageID, tiffDataID);
        this.posY = md.getPlanePositionY(imageID, tiffDataID);
        this.posZ = md.getPlanePositionZ(imageID, tiffDataID);
        
        this.imageModel = imageModel;
    }

    @Override
    public String toString() {
        return "TiffData : C = " + c + " | Z = " + z + " | T = " + t;
    }

    public ImageModel getImageModel() {
        return imageModel;
    }

}
