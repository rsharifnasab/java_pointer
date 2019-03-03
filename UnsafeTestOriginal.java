import java.lang.reflect.Field;
import java.util.Random;

import sun.misc.Unsafe;

/**
 * How to write directly to a memory.
 * 
 * @author itcuties
 * 
 */
public class  UnsafeTestOriginal {
  public static void main(String[] args) {
	// Uncomment to show how to read/write bytes
	//showBytes();

	// Uncomment to show how to read/write long values
	 showLong();

	// Uncomment to show what happens when you don't free the memory
	// showDontFreeMemory();

	// Uncomment to show how does program run while the memory is being
	// deallocated
	// showFreeMemory();

	// Uncomment to show what happens when you allocate to much memory
	showAllocateTooMuch();
  }

  /**
   * This method shows how to read/write bytes to the memory.
   */
  public static void showBytes() {
    try {
	Unsafe unsafe = getUnsafe();

	// Writing to a memory - MAX VALUE Byte
	byte value = Byte.MAX_VALUE;
	long bytes = 1;
	// Allocate given memory size
	long memoryAddress = unsafe.allocateMemory(bytes);
	// Write value to the allocated memory
	unsafe.putAddress(memoryAddress, value); // or putByte

	// Output the value written and the memory address
	System.out.println("[Byte] Writing " + value + " under the " + memoryAddress + " address.");

	long readValue = unsafe.getAddress(memoryAddress); // or getByte

	// Output the value from
	System.out.println("[Byte] Reading " + readValue + " from the " + memoryAddress + " address.");

	// C style! Release the Kraken... Memory!! :)
	unsafe.freeMemory(memoryAddress);

	} catch (Exception e) {
		e.printStackTrace();
	}
  }

  /**
   * This method show how to read/write Long values to the memory.
   */
  private static void showLong() {
    try {
	Unsafe unsafe = getUnsafe();

	// Writing to a memory - MAX VALUE of Long
	long value = Long.MAX_VALUE;
	long bytes = Long.SIZE;
	// Allocate given memory size
	long memoryAddress = unsafe.allocateMemory(bytes);
	// Write value to the allocated memory
	unsafe.putLong(memoryAddress, value);

	// Output the value written and the memory address
	System.out.println("[Long] Writing " + value + " under the " + memoryAddress + " address.");

	// Read the value from the memory
	long readValue = unsafe.getLong(memoryAddress);

	// Output the value from
	System.out.println("[Long] Reading " + readValue + " from the " + memoryAddress + " address.");

	// C style! Release the Kraken... Memory!! :)
	unsafe.freeMemory(memoryAddress);

    } catch (Exception e) {
	e.printStackTrace();
    }
  }

  /**
   * This method show what happens when you don't deallocate memory. We start
   * 100 threads where each one is allocating memory for a Long value and
   * writes it 1000000 times without deallocating memory.
   */
  public static void showDontFreeMemory() {
    for (int t = 0; t < 100; t++) {
	new Thread() {
		public void run() {
		  System.out.println("Thread " + Thread.currentThread().getName() + " start!");
			for (int i = 0; i < 1000000; i++) {
			  try {
				Unsafe unsafe = getUnsafe();

				// Writing random Long to a memory
				long value = new Random().nextLong();
				long bytes = Long.SIZE;
				// Allocate given memory size
				long memoryAddress = unsafe.allocateMemory(bytes);
				// Write value to the allocated memory
				unsafe.putLong(memoryAddress, value);

				// Read the value from the memory
				long readValue = unsafe.getLong(memoryAddress);

				// Always free the memory !!
				// ... FIXME: deallocate the memory used

			    } catch (Exception e) {
				e.printStackTrace();
			    }
			}

		  System.out.println("Thread " + Thread.currentThread().getName() + " stop!");
		};

	  }.start();
	}
  }

  /**
   * This method code shows how you should properly allocate and deallocate
   * memory. We start 100 threads where each one is allocating memory for a
   * Long value and writes it 1000000 times with deallocating memory.
   */
  public static void showFreeMemory() {
    for (int t = 0; t < 100; t++) {
	new Thread() {

	    public void run() {
		System.out.println("Thread " + Thread.currentThread().getName() + " start!");
		for (int i = 0; i < 1000000; i++) {
		  try {
			Unsafe unsafe = getUnsafe();

			// Writing random Long to a memory
			long value = new Random().nextLong();
			long bytes = Long.SIZE;
			// Allocate given memory size
			long memoryAddress = unsafe.allocateMemory(bytes);
			// Write value to the allocated memory
			unsafe.putLong(memoryAddress, value);

			// Read the value from the memory
			long readValue = unsafe.getLong(memoryAddress);

			// Always free the memory !!
			unsafe.freeMemory(memoryAddress);

		   } catch (Exception e) {
			e.printStackTrace();
	  	   }
		}

	        System.out.println("Thread " + Thread.currentThread().getName() + " stop!");
		};

	  }.start();
	}
  }

  /**
   * This method shows what happens when you try to allocate to much memory at
   * a time.
   */
  public static void showAllocateTooMuch() {
	try {
		Unsafe unsafe = getUnsafe();

		long bytes = Integer.MAX_VALUE; // It's way too much memory!!
		// Allocate given memory size
		long memoryAddress = unsafe.allocateMemory(bytes);

	 } catch (Exception e) {
		e.printStackTrace();
	 }
  }

  /**
   * Get the Unsafe object instance.
   * 
   * @return
   * @throws Exception
   */
  private static Unsafe getUnsafe() throws Exception {
	// Get the Unsafe object instance
	Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
	field.setAccessible(true);
	return (sun.misc.Unsafe) field.get(null);
  }

}
