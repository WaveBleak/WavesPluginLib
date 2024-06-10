package dk.wavebleak.wavespluginlib.labymodhelpers;

import lombok.Getter;

@Getter
public enum EnumBalanceType {
    CASH("cash"),
    BANK("bank");

    private final String key;

    EnumBalanceType(String key) {
        this.key = key;
    }
}
