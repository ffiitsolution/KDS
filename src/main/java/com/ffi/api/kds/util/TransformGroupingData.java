package com.ffi.api.kds.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransformGroupingData {
    public static List<Map<String, Object>> transformData(List<Map<String, Object>> dataList, List<String> keyNames,
            String property) {

        Map<String, Function<Map<String, Object>, Object>> keyGetters = new LinkedHashMap<>();
        for (String keyName : keyNames) {
            keyGetters.put(keyName, data -> data.get(keyName));
        }
        return dataList.stream()
                .collect(Collectors.groupingBy(data -> createKey(keyGetters, data), Collectors.toList()))
                .entrySet().stream()
                .map(entry -> transformEntry(entry.getKey(), entry.getValue(), keyNames, property))
                .collect(Collectors.toList());
    }

    private static String createKey(Map<String, Function<Map<String, Object>, Object>> keyGetters,
            Map<String, Object> data) {
        StringBuilder keyBuilder = new StringBuilder();
        for (Map.Entry<String, Function<Map<String, Object>, Object>> entry : keyGetters.entrySet()) {
            keyBuilder.append(entry.getValue().apply(data));
        }
        return keyBuilder.toString().substring(0, keyBuilder.length() - 1); // Remove trailing hyphen
    }

    private static Map<String, Object> transformEntry(String key, List<Map<String, Object>> dataList,
            List<String> keyNames, String property) {
        Map<String, Object> transformedObject = new HashMap<>();
        for (int i = 0; i < keyNames.size(); i++) {
            transformedObject.put(keyNames.get(i), dataList.get(0).get(keyNames.get(i)));
        }
        List<Map<String, Object>> details = new ArrayList<>();
        for (Map<String, Object> data : dataList) {
            details.add(new HashMap<>(data)); // Create a copy to avoid modifying original data
        }
        transformedObject.put(property, details);
        return transformedObject;
    }
}
