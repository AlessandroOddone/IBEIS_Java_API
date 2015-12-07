package edu.uic.ibeis_java_api.values;

import edu.uic.ibeis_java_api.exceptions.InvalidSpeciesException;

public enum  Species {
    CHEETAH("cheetah"),
    ELEPHANT_SAV("elephant_savanna"),
    FROGS("frogs"),
    GIRAFFE("giraffe"),
    GIRAFFE_MASAI("giraffe_masai"),
    HYENA("hyena"),
    JAG("jaguar"),
    LEOPARD("leopard"),
    LION("lion"),
    LIONFISH("lionfish"),
    NAUT("nautilus"),
    POLAR_BEAR("bear_polar"),
    RHINO_BLACK("rhino_black"),
    RHINO_WHITE("rhino_white"),
    SEALS_RINGED("seals_saimma_ringed"),
    SEALS_SPOTTED("seals_spotted"),
    SNAILS("snails"),
    SNOW_LEOPARD("snow_leopard"),
    TIGER("tiger"),
    UNKNOWN("____"),
    WATER_BUFFALO("water_buffalo"),
    WHALESHARK("whale_shark"),
    WILDDOG("wild_dog"),
    WILDEBEEST("wildebeest"),
    WYTOADS("toads_wyoming"),
    ZEBRA_GREVY("zebra_grevys"),
    ZEBRA_PLAIN("zebra_plains");
    
    private String value;
    
    Species(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Species fromValue(String value) throws InvalidSpeciesException {
        for (Species s : Species.values()) {
            if (s.getValue().equals(value)) {
                return s;
            }
        }
        throw new InvalidSpeciesException();
    }

    @Override
    public String toString() {
        return value;
    }

}
