package com.jinyumeng.hosei_sakai.hoppii

enum class HoppiiURLs(val string: String) {
    Root("https://hoppii.hosei.ac.jp/"),
    RequestLogin("https://hoppii.hosei.ac.jp/sakai-login-tool/container"), // Will jump to the SSO page.
    HoseiSSO("https://idp.hosei.ac.jp/idp/profile/SAML2/Redirect/SSO"),
    Home("https://hoppii.hosei.ac.jp/portal"),

    SakaiAPIDoc("https://hoppii.hosei.ac.jp/direct/describe"),
    AnnouncementsFromSite("https://hoppii.hosei.ac.jp/direct/announcement/site/%@.json"),
    ContentsFromSite("https://hoppii.hosei.ac.jp/direct/content/site/%@.json"),

    // HTML pages
    AssignmentDetail("https://hoppii.hosei.ac.jp/direct/assignment/%s");
    fun with(vararg id: String) = String.format(string, *id)
}