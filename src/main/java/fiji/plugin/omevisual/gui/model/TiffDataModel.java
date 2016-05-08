/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fiji.plugin.omevisual.gui.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loci.formats.ome.OMEXMLMetadata;
import net.imagej.axis.Axes;
import net.imagej.axis.Axis;
import net.imagej.axis.AxisType;
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

        if (tiffDataID < md.getPlaneCount(imageID)) {
            this.dt = md.getPlaneDeltaT(imageID, tiffDataID);
            this.exposureTime = md.getPlaneExposureTime(imageID, tiffDataID);
            this.posX = md.getPlanePositionX(imageID, tiffDataID);
            this.posY = md.getPlanePositionY(imageID, tiffDataID);
            this.posZ = md.getPlanePositionZ(imageID, tiffDataID);
        } else {
            this.dt = null;
            this.exposureTime = null;
            this.posX = null;
            this.posY = null;
            this.posZ = null;
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
        rows.add(Arrays.asList("String A", "String B"));
        rows.add(Arrays.asList("String A", "String B"));
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
