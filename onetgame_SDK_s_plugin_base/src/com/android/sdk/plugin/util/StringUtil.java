package com.android.sdk.plugin.util;

//import java.math.BigDecimal;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;


/**
 * Some common string manipulation utilities.
 * 
 */
public final class StringUtil
{

    public static final String EMPTY_STRING = "";
//    public static final String DEF_DEVID_STRING = "";
//    public static final String DEF_STRING = Constant.getStringFromTable(new int[]{13});//"0";

    public static String makeSafe(String s)
    {
        return (s == null) ? EMPTY_STRING : s;
    }

//    public static boolean stringInArray(String str, String[] array)
//    {
//        if (array == null || array.length == 0)
//        {
//            return false;
//        }
//
//        for (int i = 0; i < array.length; i++)
//        {
//            String aSource = array[i];
//            if (aSource.equals(str))
//            {
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * 
     * @return
     */
//    public static String mulNumericalValueToString(String fee)
//    {
//        Double d = Double.valueOf(fee);
//        BigDecimal bigDecimal = new BigDecimal(d);
//        bigDecimal = bigDecimal.multiply(new BigDecimal(100));
//        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_UP);
//        int i = bigDecimal.intValue();
//        return String.valueOf(i);
//    }

    /**
     * 
     * @return
     */
//    public static String mulNumericalValueWithoutPointToString(String fee)
//    {
//        BigDecimal bigDecimal = new BigDecimal(fee);
//        bigDecimal = bigDecimal.multiply(new BigDecimal(100));
//        bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_UP);
//        return bigDecimal.toString();
//    }

    /**
     * 
     * @return
     */
//    public static String divNumericalValueToString(String fee)
//    {
//        if (fee != null && !"".equals(fee.trim()))
//        {
//            BigDecimal bigDecimal = new BigDecimal(fee);
//            return bigDecimal.divide(new BigDecimal(Constant.getStringFromTable(new int[]{83,13,13})),//"100"), 
//            2,
//                    BigDecimal.ROUND_UP).toString();
//        }
//        else
//        {
//            return Constant.getStringFromTable(new int[]{13});//"0";
//        }
//    }

    /**
     * 
     * @return
     */
//    public static String valueMulValueToString(String fee1, String fee2)
//    {
//        BigDecimal bigDecimal1 = new BigDecimal(0);
//        if (fee1 != null && !"".equals(fee1.trim()))
//            bigDecimal1 = new BigDecimal(fee1);
//        BigDecimal bigDecimal2 = new BigDecimal(0);
//        if (fee2 != null && !"".equals(fee2.trim()))
//            bigDecimal2 = new BigDecimal(fee2);
//        bigDecimal1 = bigDecimal1.multiply(bigDecimal2);
//        bigDecimal1 = bigDecimal1.setScale(0, BigDecimal.ROUND_UP);
//        return bigDecimal1.toString();
//    }

    /**
     * 
     * @return
     */
//    public static String valueMulValueToStringRoundDown2Yuan(String fee1,
//            String fee2)
//    {
//        BigDecimal bigDecimal1 = new BigDecimal(0);
//        if (fee1 != null && !"".equals(fee1.trim()))
//            bigDecimal1 = new BigDecimal(fee1);
//        BigDecimal bigDecimal2 = new BigDecimal(0);
//        if (fee2 != null && !"".equals(fee2.trim()))
//            bigDecimal2 = new BigDecimal(fee2);
//        bigDecimal1 = bigDecimal1.multiply(bigDecimal2);
//        bigDecimal1 = bigDecimal1.setScale(0, BigDecimal.ROUND_DOWN);
//        return bigDecimal1.toString();
//    }

    /**
     * 
     * @return
     */
//    public static String valueMulValueToStringRoundUp2Yuan(String fee1,
//            String fee2)
//    {
//        BigDecimal bigDecimal1 = new BigDecimal(0);
//        if (fee1 != null && !"".equals(fee1.trim()))
//            bigDecimal1 = new BigDecimal(fee1);
//        BigDecimal bigDecimal2 = new BigDecimal(0);
//        if (fee2 != null && !"".equals(fee2.trim()))
//            bigDecimal2 = new BigDecimal(fee2);
//        bigDecimal1 = bigDecimal1.multiply(bigDecimal2);
//        bigDecimal1 = bigDecimal1.setScale(2, BigDecimal.ROUND_UP);
//        return bigDecimal1.toString();
//    }

    /**
     * 
     * @return
     */
//    public static String valueAddValueToString(String fee1, String fee2)
//    {
//        BigDecimal bigDecimal1 = new BigDecimal(0);
//        if (fee1 != null && !"".equals(fee1.trim()))
//            bigDecimal1 = new BigDecimal(fee1);
//        BigDecimal bigDecimal2 = new BigDecimal(0);
//        if (fee2 != null && !"".equals(fee2.trim()))
//            bigDecimal2 = new BigDecimal(fee2);
//        bigDecimal1 = bigDecimal1.add(bigDecimal2);
//        return bigDecimal1.toString();
//    }

    /**
     * 
     * @return
     */
//    public static String valueSubValueToString(String fee1, String fee2)
//    {
//        BigDecimal bigDecimal1 = new BigDecimal(0);
//        if (fee1 != null && !"".equals(fee1.trim()))
//            bigDecimal1 = new BigDecimal(fee1);
//        BigDecimal bigDecimal2 = new BigDecimal(0);
//        if (fee2 != null && !"".equals(fee2.trim()))
//            bigDecimal2 = new BigDecimal(fee2);
//        bigDecimal1 = bigDecimal1.subtract(bigDecimal2);
//        return bigDecimal1.toString();
//    }

    /**
     * 
     * @return
     */
//    public static String valueMulValueToString2(String fee1, String fee2)
//    {
//        BigDecimal bigDecimal1 = new BigDecimal(0);
//        if (fee1 != null && !"".equals(fee1.trim()))
//            bigDecimal1 = new BigDecimal(fee1);
//        BigDecimal bigDecimal2 = new BigDecimal(0);
//        if (fee2 != null && !"".equals(fee2.trim()))
//            bigDecimal2 = new BigDecimal(fee2);
//        bigDecimal1 = bigDecimal1.multiply(bigDecimal2);
//        bigDecimal1 = bigDecimal1.setScale(2, BigDecimal.ROUND_UP);
//        return bigDecimal1.toString();
//    }

    /**
     * 
     * @return
     */
//    public static int valueSubValueToInt(String fee1, String fee2)
//    {
//        BigDecimal bigDecimal1 = new BigDecimal(0);
//        if (fee1 != null && !"".equals(fee1.trim()))
//            bigDecimal1 = new BigDecimal(fee1);
//        BigDecimal bigDecimal2 = new BigDecimal(0);
//        if (fee2 != null && !"".equals(fee2.trim()))
//            bigDecimal2 = new BigDecimal(fee2);
//        bigDecimal1 = bigDecimal1.subtract(bigDecimal2);
//        return bigDecimal1.intValue();
//    }

//    public static String Yuan2Fen(String yuan)
//    {
//        String result = Constant.getStringFromTable(new int[]{13});//"0";
//        BigDecimal bd = null;
//        if (yuan != null && !"".equals(yuan.trim()))
//        {
//            bd = new BigDecimal(yuan);
//            bd = bd.multiply(new BigDecimal(100));
//            bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
//            result = bd.toString();
//        }
//        return result;
//    }
//
//    public static String Fen2Yuan(String fen)
//    {
//        String result = "";
//        BigDecimal bd = null;
//        if (fen != null && !"".equals(fen.trim()))
//        {
//            bd = new BigDecimal(fen);
//            bd = bd.divide(new BigDecimal(100));
//            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
//            result = bd.toString();
//        }
//        return result;
//    }
//    
//    public static String Fen2Yuan_int(String fen)
//    {
//        String result = "";
//        BigDecimal bd = null;
//        if (fen != null && !"".equals(fen.trim()))
//        {
//            bd = new BigDecimal(fen);
//            bd = bd.divide(new BigDecimal(100));
//            bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
//            result = bd.toString();
//        }
//        return result;
//    }

    /**
     * 
     * @param str
     * @return
     */
//    public static boolean stringFilter(String str)
//    {
//        Pattern pattern = Pattern
//                .compile(Constant.getStringFromTable(new int[]{26,52,52,47,83,51,77,18,8,35,46,13,22,77,42,20,2,13,20,52,8,49,8,35,46,83,22,33,42,20,85,38}));//"^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$");
//        Matcher m = pattern.matcher(str);
//        return m.matches();
//    }
}
