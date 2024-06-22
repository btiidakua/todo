package com.example.todo.domain.service.todo;

import java.util.ArrayList;
import java.util.List;

public class StackTraceSample {

    public static void execute() throws Exception {
        try {
            String ret = method1();
            if (!"3".equals(ret)) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static String method1() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(Integer.toString(i));
        }
        String ret = method2(list);
        return ret;
    }

    private static String method2(List<String> arg) {
        List<String> list = List.of("l", "2", "3");
        return method3(arg, list);
    }

    private static String method3(List<String> arg1, List<String> arg2) {
        List<String> list1 = method4(arg1);
        Integer number = Integer.valueOf(9);
        for (String str : list1) {
            number = Integer.parseInt(str);
        }
        List<String> list2 = method4(arg2);
        for (String str : list2) {
            number = Integer.parseInt(str);
        }
        return Integer.toString(number);
    }

    private static List<String> method4(List<String> arg) {

        List<String> ret = new ArrayList<>();
        for (int i = 0; i < arg.size(); i++) {
            switch (arg.get(i)) {
                case "1":
                    ret.add("A");
                    break;
                case "2":
                    ret.add("B");
                    break;
                case "3":
                default:
                    ret.add("9");
                    break;
            }
        }
        return arg;
    }
}
