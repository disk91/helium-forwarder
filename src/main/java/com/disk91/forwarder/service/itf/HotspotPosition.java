package com.disk91.forwarder.service.itf;

import com.disk91.forwarder.service.itf.sub.LatLng;
import fr.ingeniousthings.tools.ClonnableObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Hotspot Position", description = "Information related to an hotspot")
public class HotspotPosition implements ClonnableObject<HotspotPosition> {

    @Schema(
            description = "Hexstring Base58 of the Hotspot public key, aka Hs address",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String hotspotId;

    @Schema(
            description = "Animal name of the hostpot with - between words",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String animalName;


    @Schema(
            description = "Hotspot position, lat / lng, can be 0,0",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LatLng position;


    // ---------------------------------------------------------
    // init

    public HotspotPosition clone() {
        HotspotPosition h = new HotspotPosition();
        h.setHotspotId(this.getHotspotId());
        h.setAnimalName(this.getAnimalName());
        h.setPosition(position.clone());
        return h;
    }

    // -------------------
    // Getter & Setters


    public String getHotspotId() {
        return hotspotId;
    }

    public void setHotspotId(String hotspotId) {
        this.hotspotId = hotspotId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }
}
