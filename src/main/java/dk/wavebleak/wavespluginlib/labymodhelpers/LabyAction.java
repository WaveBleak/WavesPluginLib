package dk.wavebleak.wavespluginlib.labymodhelpers;

import lombok.Getter;

@Getter
public class LabyAction {

    private final String displayName;
    private final EnumActionType type;
    private final String value;
    public LabyAction(String displayName, EnumActionType type, String value) {
        this.displayName = displayName;
        this.type = type;
        this.value = value;
    }

}
