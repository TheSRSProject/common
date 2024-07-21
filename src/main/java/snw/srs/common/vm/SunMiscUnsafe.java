package snw.srs.common.vm;

import lombok.SneakyThrows;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public final class SunMiscUnsafe extends Unsafe {
    public static final SunMiscUnsafe INSTANCE;

    static {
        INSTANCE = new SunMiscUnsafe();
    }

    private final sun.misc.Unsafe theUnsafe;
    private final Module allUnnamedModule;
    private final Module everyoneModule;
    private final MethodHandles.Lookup implLookup;
    private final MethodHandle addExportHandle;
    private final MethodHandle addOpensHandle;

    @SuppressWarnings("ExtractMethodRecommender")
    @SneakyThrows
    private SunMiscUnsafe() {
        // According to the following articles:
        // "What Are Internal APIs?" section of https://dev.java/learn/modules/strong-encapsulation/
        // "What about Unsafe?" section of
        // https://blogs.oracle.com/javamagazine/post/a-peek-into-java-17-continuing-the-drive-to-encapsulate-the-java-runtime-internals
        // The sun.misc package is exported and opened by JDK, so we could access it
        final Field theUnsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeField.setAccessible(true);
        theUnsafe = (sun.misc.Unsafe) theUnsafeField.get(null);

        final Field implLookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
        implLookup = (MethodHandles.Lookup) getStatic0(implLookupField);

        allUnnamedModule = (Module) getStatic(Module.class, "ALL_UNNAMED_MODULE", Module.class);
        everyoneModule = (Module) getStatic(Module.class, "EVERYONE_MODULE", Module.class);

        final Method implAddExportsMethod = Module.class.
                getDeclaredMethod("implAddExports", String.class, Module.class);
        addExportHandle = getUnlimitedMethodHandleOf(implAddExportsMethod);

        final Method implAddOpensMethod = Module.class.
                getDeclaredMethod("implAddOpens", String.class, Module.class);
        addOpensHandle = getUnlimitedMethodHandleOf(implAddOpensMethod);
    }

    @SneakyThrows
    @Override
    public MethodHandle getUnlimitedMethodHandleOf(Method method) {
        return getImplLookup().unreflect(method);
    }

    @Override
    public void getAccessOfField(Field field) {
        final Class<?> declaringClass = field.getDeclaringClass();
        giveReflectiveAccessToEveryone(declaringClass.getPackageName(), declaringClass.getModule());
        field.setAccessible(true);
    }

    @Override
    public void giveReflectiveAccessToEveryone(String packageName, Module targetModule) {
        giveReflectiveAccessTo(everyoneModule, packageName, targetModule);
    }

    @Override
    public void giveReflectiveAccessToAllUnnamed(String packageName, Module targetModule) {
        giveReflectiveAccessTo(allUnnamedModule, packageName, targetModule);
    }

    @SneakyThrows
    @Override
    public void giveReflectiveAccessTo(Module requesterModule, String packageName, Module targetModule) {
        // don't use implAddOpens(String) and implAddExports(String) because they are "for JDK tests only"
        addExportHandle.invoke(targetModule, packageName, requesterModule);
        addOpensHandle.invoke(targetModule, packageName, requesterModule);
    }

    @Override
    public Object getStatic(Field field) {
        // ensure the class to be initialized to make sure Unsafe will work correctly
        ensureClassInitialized(field.getDeclaringClass());
        return getStatic0(field);
    }

    private Object getStatic0(Field field) {
        final Object base = theUnsafe.staticFieldBase(field);
        final long offset = theUnsafe.staticFieldOffset(field);
        return getObject(base, offset);
    }

    @SneakyThrows
    @Override
    public Object getStatic(Class<?> from, String name, Class<?> type) {
        // ensure the class to be initialized to make sure Unsafe will work correctly
        ensureClassInitialized(from);
        return getImplLookup().findStaticGetter(from, name, type).invoke();
    }

    @Override
    public Object getObject(Object owner, long offset) {
        return theUnsafe.getObject(owner, offset);
    }

    @Override
    public void putObject(Object owner, long offset, Object newValue) {
        theUnsafe.putObject(owner, offset, newValue);
    }

    @Override
    public void putObjectVolatile(Object owner, long offset, Object newValue) {
        theUnsafe.putObjectVolatile(owner, offset, newValue);
    }

    @SneakyThrows
    @Override
    public void ensureClassInitialized(Class<?> clazz) {
        getImplLookup().ensureInitialized(clazz);
    }

    @Override
    public MethodHandles.Lookup getImplLookup() {
        return implLookup;
    }

    @Override
    public long objectFieldOffset(Field field) {
        return theUnsafe.objectFieldOffset(field);
    }

    @Override
    public Object staticFieldBase(Field field) {
        return theUnsafe.staticFieldBase(field);
    }

    @Override
    public long staticFieldOffset(Field field) {
        return theUnsafe.staticFieldOffset(field);
    }

    @Override
    public int arrayBaseOffset(Class<?> arrayClass) {
        return theUnsafe.arrayBaseOffset(arrayClass);
    }

    @Override
    public boolean getBooleanVolatile(Object o, long offset) {
        return theUnsafe.getBooleanVolatile(o, offset);
    }

    @Override
    public void putByte(Object o, long offset, byte x) {
        theUnsafe.putByte(o, offset, x);
    }

    @Override
    public long getAndSetLong(Object o, long offset, long newValue) {
        return theUnsafe.getAndSetLong(o, offset, newValue);
    }

    @Override
    public byte getByte(long address) {
        return theUnsafe.getByte(address);
    }

    @Override
    public void putFloatVolatile(Object o, long offset, float x) {
        theUnsafe.putFloatVolatile(o, offset, x);
    }

    @Override
    public void putIntVolatile(Object o, long offset, int x) {
        theUnsafe.putIntVolatile(o, offset, x);
    }

    @Override
    public void putByte(long address, byte x) {
        theUnsafe.putByte(address, x);
    }

    @Override
    public void putAddress(long address, long x) {
        theUnsafe.putAddress(address, x);
    }

    @Override
    public long reallocateMemory(long address, long bytes) {
        return theUnsafe.reallocateMemory(address, bytes);
    }

    @Override
    public int addressSize() {
        return theUnsafe.addressSize();
    }

    @Override
    public short getShort(long address) {
        return theUnsafe.getShort(address);
    }

    @Override
    public void putDoubleVolatile(Object o, long offset, double x) {
        theUnsafe.putDoubleVolatile(o, offset, x);
    }

    @Override
    public short getShort(Object o, long offset) {
        return theUnsafe.getShort(o, offset);
    }

    @Override
    public Object getAndSetObject(Object o, long offset, Object newValue) {
        return theUnsafe.getAndSetObject(o, offset, newValue);
    }

    @Override
    public void putShort(Object o, long offset, short x) {
        theUnsafe.putShort(o, offset, x);
    }

    @Override
    public long allocateMemory(long bytes) {
        return theUnsafe.allocateMemory(bytes);
    }

    @Override
    public int arrayIndexScale(Class<?> arrayClass) {
        return theUnsafe.arrayIndexScale(arrayClass);
    }

    @Override
    public void putBooleanVolatile(Object o, long offset, boolean x) {
        theUnsafe.putBooleanVolatile(o, offset, x);
    }

    @Override
    public void putShort(long address, short x) {
        theUnsafe.putShort(address, x);
    }

    @Override
    public double getDoubleVolatile(Object o, long offset) {
        return theUnsafe.getDoubleVolatile(o, offset);
    }

    @Override
    public void putByteVolatile(Object o, long offset, byte x) {
        theUnsafe.putByteVolatile(o, offset, x);
    }

    @Override
    public char getChar(long address) {
        return theUnsafe.getChar(address);
    }

    @Override
    public void throwException(Throwable ee) {
        theUnsafe.throwException(ee);
    }

    @Override
    public void invokeCleaner(ByteBuffer directBuffer) {
        theUnsafe.invokeCleaner(directBuffer);
    }

    @Override
    public void putOrderedObject(Object o, long offset, Object x) {
        theUnsafe.putOrderedObject(o, offset, x);
    }

    @Override
    public void putChar(long address, char x) {
        theUnsafe.putChar(address, x);
    }

    @Override
    public void setMemory(Object o, long offset, long bytes, byte value) {
        theUnsafe.setMemory(o, offset, bytes, value);
    }

    @Override
    public int pageSize() {
        return theUnsafe.pageSize();
    }

    @Override
    public char getChar(Object o, long offset) {
        return theUnsafe.getChar(o, offset);
    }

    @Override
    public Object allocateInstance(Class<?> cls) throws InstantiationException {
        return theUnsafe.allocateInstance(cls);
    }

    @Override
    public byte getByteVolatile(Object o, long offset) {
        return theUnsafe.getByteVolatile(o, offset);
    }

    @Override
    public int getInt(long address) {
        return theUnsafe.getInt(address);
    }

    @Override
    public void putOrderedLong(Object o, long offset, long x) {
        theUnsafe.putOrderedLong(o, offset, x);
    }

    @Override
    public void loadFence() {
        theUnsafe.loadFence();
    }

    @Override
    public void copyMemory(Object srcBase, long srcOffset, Object destBase, long destOffset, long bytes) {
        theUnsafe.copyMemory(srcBase, srcOffset, destBase, destOffset, bytes);
    }

    @Override
    public short getShortVolatile(Object o, long offset) {
        return theUnsafe.getShortVolatile(o, offset);
    }

    @Override
    public void putChar(Object o, long offset, char x) {
        theUnsafe.putChar(o, offset, x);
    }

    @Override
    public int getInt(Object o, long offset) {
        return theUnsafe.getInt(o, offset);
    }

    @Override
    public void putInt(long address, int x) {
        theUnsafe.putInt(address, x);
    }

    @Override
    public void setMemory(long address, long bytes, byte value) {
        theUnsafe.setMemory(address, bytes, value);
    }

    @Override
    public void storeFence() {
        theUnsafe.storeFence();
    }

    @Override
    public long getLong(Object o, long offset) {
        return theUnsafe.getLong(o, offset);
    }

    @Override
    public boolean compareAndSwapObject(Object o, long offset, Object expected, Object x) {
        return theUnsafe.compareAndSwapObject(o, offset, expected, x);
    }

    @Override
    public void putOrderedInt(Object o, long offset, int x) {
        theUnsafe.putOrderedInt(o, offset, x);
    }

    @Override
    public void fullFence() {
        theUnsafe.fullFence();
    }

    @Override
    public char getCharVolatile(Object o, long offset) {
        return theUnsafe.getCharVolatile(o, offset);
    }

    @Override
    public void copyMemory(long srcAddress, long destAddress, long bytes) {
        theUnsafe.copyMemory(srcAddress, destAddress, bytes);
    }

    @Override
    public void unpark(Object thread) {
        theUnsafe.unpark(thread);
    }

    @Override
    public void putInt(Object o, long offset, int x) {
        theUnsafe.putInt(o, offset, x);
    }

    @Override
    public void putLong(Object o, long offset, long x) {
        theUnsafe.putLong(o, offset, x);
    }

    @Override
    public long getLong(long address) {
        return theUnsafe.getLong(address);
    }

    @Override
    public boolean compareAndSwapInt(Object o, long offset, int expected, int x) {
        return theUnsafe.compareAndSwapInt(o, offset, expected, x);
    }

    @Override
    public void putShortVolatile(Object o, long offset, short x) {
        theUnsafe.putShortVolatile(o, offset, x);
    }

    @Override
    public float getFloat(Object o, long offset) {
        return theUnsafe.getFloat(o, offset);
    }

    @Override
    public void freeMemory(long address) {
        theUnsafe.freeMemory(address);
    }

    @Override
    public int getLoadAverage(double[] loadavg, int nelems) {
        return theUnsafe.getLoadAverage(loadavg, nelems);
    }

    @Override
    public void putCharVolatile(Object o, long offset, char x) {
        theUnsafe.putCharVolatile(o, offset, x);
    }

    @Override
    public void putLong(long address, long x) {
        theUnsafe.putLong(address, x);
    }

    @Override
    public boolean compareAndSwapLong(Object o, long offset, long expected, long x) {
        return theUnsafe.compareAndSwapLong(o, offset, expected, x);
    }

    @Override
    public void putFloat(Object o, long offset, float x) {
        theUnsafe.putFloat(o, offset, x);
    }

    @Override
    public float getFloat(long address) {
        return theUnsafe.getFloat(address);
    }

    @Override
    public void park(boolean isAbsolute, long time) {
        theUnsafe.park(isAbsolute, time);
    }

    @Override
    public void putLongVolatile(Object o, long offset, long x) {
        theUnsafe.putLongVolatile(o, offset, x);
    }

    @Override
    public boolean getBoolean(Object o, long offset) {
        return theUnsafe.getBoolean(o, offset);
    }

    @Override
    public void putFloat(long address, float x) {
        theUnsafe.putFloat(address, x);
    }

    @Override
    public long getLongVolatile(Object o, long offset) {
        return theUnsafe.getLongVolatile(o, offset);
    }

    @Override
    public int getAndAddInt(Object o, long offset, int delta) {
        return theUnsafe.getAndAddInt(o, offset, delta);
    }

    @Override
    public double getDouble(long address) {
        return theUnsafe.getDouble(address);
    }

    @Override
    public Object getObjectVolatile(Object o, long offset) {
        return theUnsafe.getObjectVolatile(o, offset);
    }

    @Override
    public double getDouble(Object o, long offset) {
        return theUnsafe.getDouble(o, offset);
    }

    @Override
    public int getAndSetInt(Object o, long offset, int newValue) {
        return theUnsafe.getAndSetInt(o, offset, newValue);
    }

    @Override
    public void putDouble(long address, double x) {
        theUnsafe.putDouble(address, x);
    }

    @Override
    public void putDouble(Object o, long offset, double x) {
        theUnsafe.putDouble(o, offset, x);
    }

    @Override
    public void putBoolean(Object o, long offset, boolean x) {
        theUnsafe.putBoolean(o, offset, x);
    }

    @Override
    public float getFloatVolatile(Object o, long offset) {
        return theUnsafe.getFloatVolatile(o, offset);
    }

    @Override
    public long getAddress(long address) {
        return theUnsafe.getAddress(address);
    }

    @Override
    public byte getByte(Object o, long offset) {
        return theUnsafe.getByte(o, offset);
    }

    @Override
    public int getIntVolatile(Object o, long offset) {
        return theUnsafe.getIntVolatile(o, offset);
    }

    @Override
    public long getAndAddLong(Object o, long offset, long delta) {
        return theUnsafe.getAndAddLong(o, offset, delta);
    }
}