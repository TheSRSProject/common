package snw.srs.common.vm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

// Dangerous but powerful, only use it when nothing else can solve your problem.
// VM crash can be caused if you used this wrongly!
public abstract class Unsafe {

    public static Unsafe getUnsafe() {
        return SunMiscUnsafe.INSTANCE;
    }

    public abstract MethodHandle getUnlimitedMethodHandleOf(Method method);

    public abstract void getAccessOfField(Field field);

    public abstract void giveReflectiveAccessToEveryone(String packageName, Module targetModule);

    public abstract void giveReflectiveAccessToAllUnnamed(String packageName, Module targetModule);

    public abstract void giveReflectiveAccessTo(Module requesterModule, String packageName, Module targetModule);

    public abstract Object getStatic(Field field);

    public abstract Object getStatic(Class<?> from, String name, Class<?> type);

    public abstract Object getObject(Object owner, long offset);

    public abstract void putObject(Object owner, long offset, Object newValue);

    public abstract void putObjectVolatile(Object owner, long offset, Object newValue);

    public abstract void ensureClassInitialized(Class<?> clazz);

    public abstract MethodHandles.Lookup getImplLookup();

    public abstract long objectFieldOffset(Field field);

    public abstract Object staticFieldBase(Field field);

    public abstract long staticFieldOffset(Field field);

    public abstract int arrayBaseOffset(Class<?> arrayClass);

    public abstract boolean getBooleanVolatile(Object o, long offset);

    public abstract void putByte(Object o, long offset, byte x);

    public abstract long getAndSetLong(Object o, long offset, long newValue);

    public abstract byte getByte(long address);

    public abstract void putFloatVolatile(Object o, long offset, float x);

    public abstract void putIntVolatile(Object o, long offset, int x);

    public abstract void putByte(long address, byte x);

    public abstract void putAddress(long address, long x);

    public abstract long reallocateMemory(long address, long bytes);

    public abstract int addressSize();

    public abstract short getShort(long address);

    public abstract void putDoubleVolatile(Object o, long offset, double x);

    public abstract short getShort(Object o, long offset);

    public abstract Object getAndSetObject(Object o, long offset, Object newValue);

    public abstract void putShort(Object o, long offset, short x);

    public abstract long allocateMemory(long bytes);

    public abstract int arrayIndexScale(Class<?> arrayClass);

    public abstract void putBooleanVolatile(Object o, long offset, boolean x);

    public abstract void putShort(long address, short x);

    public abstract double getDoubleVolatile(Object o, long offset);

    public abstract void putByteVolatile(Object o, long offset, byte x);

    public abstract char getChar(long address);

    public abstract void throwException(Throwable ee);

    public abstract void invokeCleaner(ByteBuffer directBuffer);

    public abstract void putOrderedObject(Object o, long offset, Object x);

    public abstract void putChar(long address, char x);

    public abstract void setMemory(Object o, long offset, long bytes, byte value);

    public abstract int pageSize();

    public abstract char getChar(Object o, long offset);

    public abstract Object allocateInstance(Class<?> cls) throws InstantiationException;

    public abstract byte getByteVolatile(Object o, long offset);

    public abstract int getInt(long address);

    public abstract void putOrderedLong(Object o, long offset, long x);

    public abstract void loadFence();

    public abstract void copyMemory(Object srcBase, long srcOffset, Object destBase, long destOffset, long bytes);

    public abstract short getShortVolatile(Object o, long offset);

    public abstract void putChar(Object o, long offset, char x);

    public abstract int getInt(Object o, long offset);

    public abstract void putInt(long address, int x);

    public abstract void setMemory(long address, long bytes, byte value);

    public abstract void storeFence();

    public abstract long getLong(Object o, long offset);

    public abstract boolean compareAndSwapObject(Object o, long offset, Object expected, Object x);

    public abstract void putOrderedInt(Object o, long offset, int x);

    public abstract void fullFence();

    public abstract char getCharVolatile(Object o, long offset);

    public abstract void copyMemory(long srcAddress, long destAddress, long bytes);

    public abstract void unpark(Object thread);

    public abstract void putInt(Object o, long offset, int x);

    public abstract void putLong(Object o, long offset, long x);

    public abstract long getLong(long address);

    public abstract boolean compareAndSwapInt(Object o, long offset, int expected, int x);

    public abstract void putShortVolatile(Object o, long offset, short x);

    public abstract float getFloat(Object o, long offset);

    public abstract void freeMemory(long address);

    public abstract int getLoadAverage(double[] loadavg, int nelems);

    public abstract void putCharVolatile(Object o, long offset, char x);

    public abstract void putLong(long address, long x);

    public abstract boolean compareAndSwapLong(Object o, long offset, long expected, long x);

    public abstract void putFloat(Object o, long offset, float x);

    public abstract float getFloat(long address);

    public abstract void park(boolean isAbsolute, long time);

    public abstract void putLongVolatile(Object o, long offset, long x);

    public abstract boolean getBoolean(Object o, long offset);

    public abstract void putFloat(long address, float x);

    public abstract long getLongVolatile(Object o, long offset);

    public abstract int getAndAddInt(Object o, long offset, int delta);

    public abstract double getDouble(long address);

    public abstract Object getObjectVolatile(Object o, long offset);

    public abstract double getDouble(Object o, long offset);

    public abstract int getAndSetInt(Object o, long offset, int newValue);

    public abstract void putDouble(long address, double x);

    public abstract void putDouble(Object o, long offset, double x);

    public abstract void putBoolean(Object o, long offset, boolean x);

    public abstract float getFloatVolatile(Object o, long offset);

    public abstract long getAddress(long address);

    public abstract byte getByte(Object o, long offset);

    public abstract int getIntVolatile(Object o, long offset);

    public abstract long getAndAddLong(Object o, long offset, long delta);
}