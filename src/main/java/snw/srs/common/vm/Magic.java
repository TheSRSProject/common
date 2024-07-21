package snw.srs.common.vm;

import lombok.SneakyThrows;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class Magic {
    public static final StackWalker STACK_WALKER;
    private static final MethodHandle MODIFIER_SETTER;

    static {
        STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        MODIFIER_SETTER = getModifiersSetter();
    }

    @SneakyThrows
    private static MethodHandle getModifiersSetter() {
        Unsafe unsafe = Unsafe.getUnsafe();
        return unsafe.getImplLookup()
                .findSetter(Field.class, "modifiers", int.class);
    }

    private Magic() {
    }

    // this magic method ignores the final modifier
    @SneakyThrows
    public static void forceSetField(Field field, Object obj, Object value) {
        Unsafe.getUnsafe().getAccessOfField(field);
        int modifiers = field.getModifiers();
        int newModifiers = modifiers & ~Modifier.FINAL;
        MODIFIER_SETTER.invoke(field, newModifiers);
        field.set(obj, value);
        MODIFIER_SETTER.invoke(field, modifiers);
    }
}
