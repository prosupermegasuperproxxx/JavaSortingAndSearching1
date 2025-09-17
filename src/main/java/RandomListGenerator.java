import java.util.*;

public class RandomListGenerator {

    static java.text.DecimalFormat df = new java.text.DecimalFormat("#.###", new java.text.DecimalFormatSymbols(Locale.US));

    public static void main(String[] args) {
//        variant1();
        generateTestList(50);
    }

    private static void generateTestList(int size) {

        Random random = new Random();
        String[] names = {"   чётный", "НЕ чётный"};
        List<String> dataList = new ArrayList<>();

        int max = String.valueOf(size).length();

        int desiredLength = max;


        for (int i = 0; i < size; i++) {
            int i1 = random.nextInt(names.length);
            int age = random.nextInt(100) + 1;
            
            if(i1%2==0) {
                if (age % 2 != 0)
                    age++;
            } else
            if (age % 2 == 0)
                age++;
            
            String name = names[i1];

            // Используем String.format для заполнения нулями
            String formattedWord = String.format("%" + desiredLength + "s", String.valueOf(i)).replace(' ', '0');

            double salary = random.nextDouble() * 100000;

            String ds = "";
            ds = String.format(Locale.US, "%.3f", salary);
            dataList.add(formattedWord + ": " + name + "," + age + ",\t " + ds);

        }

        // Выводим исходный список
        System.out.println("Исходный список:");
        for (String item : dataList) {
            System.out.println(item);
        }

        // Перемешиваем список
        Collections.shuffle(dataList, random);

        // Выводим перемешанный список
        System.out.println("\nПеремешанный список:");
        for (String item : dataList) {
            System.out.println(item);
        }
    }


    private static void variant1() {
        // Создаём список букв от A до Z
        List<String> letters = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            letters.add(String.valueOf(c));
        }

        // Генерируем случайные числа и формируем строки
        List<String> dataList = new ArrayList<>();
        Random random = new Random();

        for (String letter : letters) {
            int num1 = random.nextInt(900) + 100;     // от 100 до 999
            double num2 = random.nextDouble(7001) + 3000;   // от 3000 до 10000
            String ds = "";
//            ds = String.valueOf(num2);
//            ds = Double.toString(num2);
            ds = String.format(Locale.US, "%.3f", num2);
//            ds = df.format(num2);
            dataList.add(letter + ",\t " + num1 + ",\t " + ds);
//            dataList.add(letter + ";" + num1 + ";" + );
        }

        // Выводим исходный список
        System.out.println("Исходный список:");
        for (String item : dataList) {
            System.out.println(item);
        }

        // Перемешиваем список
        Collections.shuffle(dataList, random);

        // Выводим перемешанный список
        System.out.println("\nПеремешанный список:");
        for (String item : dataList) {
            System.out.println(item);
        }
    }
}