package com.salespilot.organization.util;

import java.text.Normalizer;
import java.util.Locale;

public final class OrganizationSlugGenerator {

    private  OrganizationSlugGenerator(){

    }

    public static String generate(String name){
         String normalised = Normalizer.normalize(name.trim().toLowerCase(Locale.ROOT), Normalizer.Form.NFKD);


        String slug = normalised
                .replaceAll("\\p{M}+", "")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        if(slug.isBlank()){
            throw new IllegalArgumentException("Organization name cannot produce a valid slug");

        }

        return slug.length() > 100 ? slug.substring(0,100).replaceAll("-$",""): slug;
    }
}
