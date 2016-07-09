package org.fs.xml.soap;

import org.fs.xml.soap.reflection.ReferenceUtility;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Fatih on 04/07/16.
 * as org.fs.xml.soap.SoapConfig
 */
public class SoapConfig {

    public static void main(String...args) {
        try {
            Object collection = new ArrayList<>();
            Collection<?> array = ReferenceUtility.castAs(collection, Collection.class);
            if (array != null) {

            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
