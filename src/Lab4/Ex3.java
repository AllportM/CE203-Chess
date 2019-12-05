package Lab4;

import java.util.*;

public class Ex3 {
    public static void replaceNegList(List<Integer> l)
    {
        int index = 0;
        for (Iterator<Integer> lIt = l.iterator(); lIt.hasNext();)
        {
            Integer inspect = lIt.next();
            if (inspect < 0) l.set(index, inspect - inspect - inspect);
            index++;
        }
    }

    public static void main(String[] args) {
        int test = 1/2;
        System.out.println(test);
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(10);
        list.add(2);
        list.add(-2);
        list.add(-10);
        list.add(-1);
        System.out.println(list);
        replaceNegList(list);
        System.out.println(list);
    }
}
