/**
 * TODO: Finalize the documentation of  "switchFile", add a default "switchFile" (cache content to true), add other classes (cf. JsonElement)
 */

package ladysnake.helpers;

import com.google.gson.*;

/**A class that allows to easily read from a JSON file
 * @author Ludwig GUERIN
 */
public class JsonReader {
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Fields
    ////////////////////////////////////////////////////////////////////////////////////////////

    /**A String holding the URI to the JSON file
     */
    protected  String fileURI;

    /**A JsonElement holding the content of the file (@see #fileURI)
     */
    protected JsonElement content;

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////

    /**Default constructor, seeking the file from its URI
     * @param URI being the URI to the JSON file which the user would like to read
     * @throws JsonParseException if ithe file is not valid JSON
     */
    JsonReader(String URI) throws JsonParseException {
        this.fileURI = URI;
        refreshContent();
    }

    /**"Copy constructor"
     * @param other being the JsonReader to copy from
     * @throws JsonParseException if the file doesn't contain valid JSON
     */
    JsonReader(JsonReader other) throws JsonParseException {
        this(other.fileURI);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////

    /**A helper method that allows to cache the file of this JsonReader
     * @return the content of the JSON file as a {@link JsonElement}
     * @throws JsonParseException
     */
    private JsonElement cacheFile() throws JsonParseException{
        return new JsonParser().parse(this.fileURI);
    }

    /**Refreshes the content of the file contained in this JsonReader with the content of the file
     * @throws JsonParseException if the file doesn't contain valid JSON
     * @return this JsonReader (for chaining purposes)
     */
    public JsonReader refreshContent() throws JsonParseException{
        this.content = cacheFile();
        return this;
    }

    /**
     *
     * @param URI
     * @param cacheContent
     * @return
     */
    public JsonReader switchFile(String URI, boolean cacheContent){
        this.fileURI = URI;
        refreshContent();
        return this;
    }

    /**Retrieve the content of the file as a {@link JsonArray}
     * @return the content of the file as a {@link JsonArray}
     * @throws IllegalStateException if the content is of another type
     */
    public JsonArray getAsArray() throws IllegalStateException{
        return this.content.getAsJsonArray();
    }

    /**Retrieve the content of the file as a {@link JsonObject}
     * @return the content of the file as a {@link JsonObject}
     * @throws IllegalStateException if the content is of another type
     */
    public JsonObject getAsObject() throws IllegalStateException{
        return this.content.getAsJsonObject();
    }

    /**Retrieve the content of the file as a {@link JsonPrimitive}
     * @return the content of the file as a {@link JsonPrimitive}
     * @throws IllegalStateException if the content is of another type
     */
    public JsonPrimitive getAsJsonPrimitive() throws IllegalStateException{
        return this.content.getAsJsonPrimitive();
    }

    /**Retrieve the content of the file as a {@link JsonNull}
     * @return the content of the file as a {@link JsonNull}
     * @throws IllegalStateException if the content is of another type
     */
    public JsonNull getAsJsonNull() throws IllegalStateException{
        return this.content.getAsJsonNull();
    }
}
