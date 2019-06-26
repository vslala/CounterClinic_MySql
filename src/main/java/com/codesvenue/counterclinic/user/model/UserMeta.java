package com.codesvenue.counterclinic.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserMeta {
    private Integer metaId;
    private Integer userId;
    private String metaKey;
    private String metaValue;

    public static UserMeta newInstance() {
        return new UserMeta();
    }

    public UserMeta metaId(int intValue) {
        this.metaId = intValue;
        return this;
    }

    public UserMeta userId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public UserMeta metaKey(String metaKey) {
        this.metaKey = metaKey;
        return this;
    }

    public UserMeta metaValue(String metaValue) {
        this.metaValue = metaValue;
        return this;
    }
}
