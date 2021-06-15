package net.oscer.framework;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @author kz
 * @create 2021-05-26 15:02
 **/
public class ConvertUtil {

    public static void main(String[] args) throws Exception {
        String classA = "com.jthe.midstrage.openplatform.Account";
        String classB = "com.jthe.midstrage.openplatform.User";
        Class ca = Class.forName(classA);
        Class cb = Class.forName(classB);
        System.out.println(ca.getDeclaredFields());
        System.out.println(cb.getDeclaredFields());
        Field[] fieldsA = ca.getDeclaredFields();
        Field[] fieldsB = cb.getDeclaredFields();

        Set<String> sets = new HashSet<>();
        for (Field A : fieldsA) {
            for (Field B : fieldsB) {
                if (StringUtils.equalsAnyIgnoreCase(A.getName(), B.getName())) {
                    System.out.println(A.getName().substring(0, 1).toUpperCase() + A.getName().substring(1));
                    sets.add(A.getName().substring(0, 1).toUpperCase() + A.getName().substring(1));
                }
            }
        }
        if (sets.size() > 0) {
            StringBuilder sb = new StringBuilder();
            String[] bb = StringUtils.split(classB, ".");
            sb.append("public void convert(").append(bb[bb.length - 1]).append(" ").
                    append(bb[bb.length - 1].toLowerCase()).append("){");
            sb.append("\n");
            for (String s : sets) {
                sb.append("\t");
                sb.append("this.set").append(s).append("(").
                        append(bb[bb.length - 1].toLowerCase().toLowerCase()).
                        append(".get").append(s).append("());");
                sb.append("\n");
            }
            sb.append("}");
            System.out.println(sb.toString());
        }
    }
}
