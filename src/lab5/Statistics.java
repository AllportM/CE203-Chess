//package lab5;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//
//public class Statistics {
//
//    static int maximum (ArrayList<Integer> lisy)
//    {
//        // Iterator version, commented out return clause
//        int max = Integer.MIN_VALUE;
//        for (Iterator<Integer> listIt = lisy.iterator(); listIt.hasNext();)
//        {
//            Integer curr = listIt.next();
//            max = curr > max ? curr : max;
//        }
//        //return max;
//
//        // for loop non Iterator version
//        int max2 = Integer.MAX_VALUE;
//        for (int i = 0; i < lisy.size(); i++)
//        {
//            max2 = lisy.get(i) > max2 ? lisy.get(i): max2;
//        }
//        return max2;
//    }
//
//    static int minimum (ArrayList<Integer> list)
//    {
//        // Iterator version, commmented out return clause
//        int min = Integer.MAX_VALUE;
//        for (Iterator<Integer> listIt = list.iterator(); listIt.hasNext();)
//        {
//            Integer curr = listIt.next();
//            min = curr < min ? curr: min;
//        }
//        //return min;
//
//        // for loop non Iterator version
//        int min2 = Integer.MAX_VALUE;
//        for (int i = 0; i < list.size(); i++)
//        {
//            min2 = list.get(i) < min2 ? list.get(i): min2;
//        }
//        return min2;
//    }
//
//    static double average (ArrayList<Integer> list)
//    {
//        // Iterator version
//        double size = list.size();
//        int sum = 0;
//        for (Iterator<Integer> listIt = list.iterator(); listIt.hasNext();)
//        {
//            sum += listIt.next();
//        }
//        //return sum/size;
//
//        // for loop non iterator version
//        sum = 0;
//        for (int i = 0; i < size; i++)
//        {
//            sum += list.get(i);
//        }
//        return sum/size;
//    }
//}
