package SO2.punto4_1;

public class Binary {
    public int out(String binary) {
        return 0;
    }

    public static void main(String[] args) {
//        String i = "0010101010111100";
//        int i2 = Integer.parseInt(i, 2);
//        int j = i2 >> 12;
//        String binary = Integer.toBinaryString(j);
//
//        System.out.println(i + "\n" + binary + "\n");


//        for (int n = 0; n < 16; n++) {
//            i += 4096; // sumamos 4096 cada iteración
//            String hex = String.format("0x%04X", i); // lo mostramos en 4 dígitos hex
//            String hex2 = String.format("0x%04X", i - 4095);
//            System.out.println(hex2 + "  →  " + hex);
//
//        }

        String hex2 = String.format("0x%04X", (4*4096));
        System.out.println(hex2);

        System.out.println("1".repeat(2));


        int a = 0x0ABC;

        System.out.println(a);
    }
}
