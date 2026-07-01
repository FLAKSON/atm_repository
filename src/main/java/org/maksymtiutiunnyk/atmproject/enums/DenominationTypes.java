package org.maksymtiutiunnyk.atmproject.enums;

import lombok.Getter;

public enum DenominationTypes {
    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50);

    @Getter
    private final long value;

    DenominationTypes(long value) {
        this.value = value;
    }

}
