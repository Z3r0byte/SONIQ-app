package eu.z3r0byteapps.soniq.Containers;

import com.google.gson.annotations.SerializedName;

public class Search {
    //@SerializedName vertelt de JSON library wat de naam in de JSON string is van de onderstaande variabele
    @SerializedName("search_id")
    private String searchId;
    private SearchResult searchResult;

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }
}
