package com.codesvenue.counterclinic.printer;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class View {

    public static final String MASTER_TEMPLATE = "master.ftl";
    private String page;
    private String component;
    private String stylesheet;
    private String script;

    public View(String page, String component) {
        this.page = page;
        this.component = component;
    }

    public View(String page, String component, String stylesheet) {
        this(page, component);
        this.stylesheet = stylesheet;
    }

    public View(String page, String component, String stylesheet, String script) {
        this(page, component, stylesheet);
        this.script = script;
    }
}
