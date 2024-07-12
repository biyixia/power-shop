package com.bjpowernode.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class WxMsgTemplate {
    private String touser;
    private String template_id;
    private String topcolor;
    private Map<String, Map<String, String>> data;

    public void setData(String key, String value) {
        if (CollectionUtils.isEmpty(data))
            data = new HashMap<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("value", value);
        map.put("color", "#173177");
        this.data.put(key, map);
    }

}
