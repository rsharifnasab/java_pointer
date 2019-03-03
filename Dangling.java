import java.lang.reflect.Field;
import sun.misc.Unsafe;
public class Dangling {
  public static void main(String[] args) {
    try {
      Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      Unsafe unsafe = (sun.misc.Unsafe) field.get(null);
      long memoryAddress = unsafe.allocateMemory(Long.SIZE);

      unsafe.putAddress(memoryAddress,1);
      System.out.println("value of "+memoryAddress+ " is: "+unsafe.getAddress(memoryAddress));

       unsafe.freeMemory(memoryAddress);
       System.out.println("memory address " + memoryAddress+ " is dangling now");

      unsafe.putAddress(memoryAddress, 2);
      long dangValue = unsafe.getAddress(memoryAddress);
      System.out.println("this is danling value : " + dangValue);

      System.out.println("bye bye");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
