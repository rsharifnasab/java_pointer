import java.lang.reflect.Field;
import java.util.Random;
import sun.misc.Unsafe;

public class UnsafeTestSimple {


  private static void danglingMaker() {
    try {
      Unsafe unsafe = getUnsafe();

      long value = Long.MAX_VALUE;
      value = 1;
      long bytes = Long.SIZE;

      long memoryAddress = unsafe.allocateMemory(bytes);

      unsafe.putAddress(memoryAddress, value);

      System.out.println("[Long] Writing " + value + " under the " + memoryAddress + " address.");

      long readValue = unsafe.getAddress(memoryAddress);

      System.out.println("[Long] Reading " + readValue + " from the " + memoryAddress + " address.");


      unsafe.freeMemory(memoryAddress);
      System.out.println("memory address " + memoryAddress+ " is dangling now");

      unsafe.putAddress(memoryAddress, 13);
      long dangValue = unsafe.getAddress(memoryAddress);
      System.out.println("this is danling value : " + dangValue);


    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    System.out.println("\nprogram started...\n");

    danglingMaker();

  }

  private static Unsafe getUnsafe() throws Exception {
    // Get the Unsafe object instance
    Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
    field.setAccessible(true);
    return (sun.misc.Unsafe) field.get(null);
  }

}
