package com.example.demo.util;

import com.gargoylesoftware.htmlunit.html.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HtmlUnitUtils {

    public void setCourier(final HtmlPage page, final String code) {
        DomNodeList<DomElement> select = page.getElementsByTagName("select");
        System.out.println("======================================");
        System.out.println(select);

        HtmlSelect htmlSelect = (HtmlSelect)select.get(0);
        System.out.println(htmlSelect);
        System.out.println(htmlSelect.getSelectedIndex());

        List<HtmlOption> options = htmlSelect.getOptions();

        for (HtmlOption o : options) {
            System.out.println(o);
            System.out.println(o.getAttribute("data-code"));
            String c = o.getAttribute("data-code");

            if (code.equals(c)) {
                System.out.println("selected!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("code" + o.getAttribute("data-code"));
                System.out.println("name" + o.getAttribute("data-name"));
                o.setSelected(true);
                break;
            }
        }

        System.out.println(htmlSelect.getSelectedIndex());
        System.out.println(htmlSelect.getOption(htmlSelect.getSelectedIndex()));

    }

    public void setInvoice(final HtmlPage page, final String invoice) {
        HtmlTextInput input = (HtmlTextInput)page.getElementById("numb");
        System.out.println("======================================");
        System.out.println(input);
        System.out.println(input.getAttribute("value"));
        input.setAttribute("value", invoice);
        System.out.println(input.getAttribute("value"));
    }

    public void click(final HtmlPage page) throws Exception {
        HtmlImageInput input = page.querySelector("._submit.sc_btn");
        System.out.println("======================================");
        System.out.println(input);
        input.click();
    }

    public void getResult(final HtmlPage page) {
        DomNode output = page.querySelector("_output");
        System.out.println("======================================");
        System.out.println("======================================");
        System.out.println("======================================");
        System.out.println(output);
    }
}
