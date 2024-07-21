package snw.srs.common.vm;

import org.junit.jupiter.api.Test;

class SunMiscUnsafeTest {

    @Test
    void testOpenPackage() {
        SunMiscUnsafe unsafe = SunMiscUnsafe.INSTANCE;
        unsafe.giveReflectiveAccessToEveryone("java.lang", String.class.getModule());
    }

}