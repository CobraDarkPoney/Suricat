package ladysnake.helpers.json;

import com.google.gson.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**A class that allows to easily read from a JSON file
 * @author Ludwig GUERIN
 * @version 1.0.0
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class JsonReader {
    final static Charset charset = StandardCharsets.UTF_8;


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
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
     * @throws IOException if the file cannot be found
     */
    public JsonReader(String URI) throws JsonParseException, IOException {
        this.fileURI = URI;
        refreshContent();
    }

    /**Copy constructor, refreshes the content within itself
     * @param other being the JsonReader to copy from
     * @throws JsonParseException if the file doesn't contain valid JSON
     * @throws IOException if the file cannot be found
     */
    public JsonReader(JsonReader other) throws JsonParseException , IOException{
        this(other.fileURI);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////

    /**A helper method that allows to cache the file of this JsonReader
     * @return the content of the JSON file as a {@link JsonElement}
     * @throws JsonParseException if the file doesn't contain valid JSON
     * @throws IOException if the file cannot be found
     */
    protected JsonElement cacheFile() throws JsonParseException,  IOException {
        final Path path = Paths.get(this.fileURI);
        final BufferedReader reader = Files.newBufferedReader(path, charset);

        JsonElement ret = new JsonParser().parse( reader );
        reader.close();
        return ret;
    }

    /**Refreshes the content of the file contained in this JsonReader with the content of the file
     * @return this JsonReader (for chaining purposes)
     *  @throws JsonParseException if the file doesn't contain valid JSON
     *  @throws  IOException if the file cannot be found
     */
    public JsonReader refreshContent() throws JsonParseException, IOException{
        this.content = cacheFile();
        return this;
    }

    /**Switch the JsonReader's current file to another
     * @param URI being the URI to the file
     * @param cacheContent being a flag determining whether the method should cache the file's content or not
     * @return this JsonReader (for chaining purposes)
     *  @throws JsonParseException if the file doesn't contain valid JSON
     *  @throws  IOException if the file cannot be found
     */
    public JsonReader switchFile(String URI, boolean cacheContent) throws JsonParseException, IOException{
        this.fileURI = URI;
        return cacheContent ? refreshContent() : this;
    }

    /**Switch the JsonReader's current file to another and caches its content
     * @param URI being the URI to the file
     * @return this JsonReader (for chaining purposes)
     *  @throws JsonParseException if the file doesn't contain valid JSON
     *  @throws  IOException if the file cannot be found
     */
    public JsonReader switchFile(String URI) throws JsonParseException, IOException{
        return this.switchFile(URI, true);
    }

    /**Retrieve the content of the file as a {@link JsonElement}
     * @return the content of the file as a {@link JsonElement}
     */
    public JsonElement getAsJsonElement(){
        return this.content.deepCopy();
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

    /**Retrieve the content of the file as a {@link Number}
     * @return the content as a {@link Number}
     * @throws ClassCastException if the content is not a JsonPrimitive or a valid number
     * @throws IllegalStateException if the content is of another type
     */
    public Number getAsNumber() throws ClassCastException, IllegalStateException{
        return this.content.getAsNumber();
    }

    /**Retrieve the content of the file as a {@link Boolean}
     * @return the content of the file as a {@link Boolean}
     * @throws ClassCastException if the content is not a JsonPrimitive
     * @throws IllegalStateException if the content is of another type
     */
    public boolean getAsBoolean() throws ClassCastException, IllegalStateException{
        return this.content.getAsBoolean();
    }

    /**Retrieve the content of the file as a {@link String}
     * @return the content of the file as a {@link String}
     * @throws ClassCastException if the content is not a JsonPrimitive
     * @throws IllegalStateException if the content is of another type
     */
    public String getAsString() throws ClassCastException, IllegalStateException{
        return this.content.getAsString();
    }

    /**Determine whether the content of the file can safely be converted to a JsonArray
     * @return TRUE if is a JsonArray, FALSE otherwise
     */
    public boolean isArray(){
        return this.content.isJsonArray();
    }

    /**Determine whether the content of the file can safely be converted to a JsonObject
     * @return TRUE if is a JsonObject, FALSE otherwise
     */
    public boolean isObject(){
        return this.content.isJsonObject();
    }

    /**Determine whether the content of the file can safely be converted to a JsonNull
     * @return TRUE if is a JsonNull, FALSE otherwise
     */
    public boolean isJsonNull(){
        return this.content.isJsonNull();
    }

    /**Determine whether the content of the file can safely be converted to a JsonPrimitive
     * @return TRUE if is a JsonPrimitive, FALSE otherwise
     */
    public boolean isJsonPrimitive(){
        return this.content.isJsonPrimitive();
    }

    /**Creates a {@link Stream} from the content of the file (if it can -> JsonArray)
     * @return a {@link Stream} if the content is a JsonArray
     * @throws InvalidClassException if the content is not a JsonArray
     */
    public Stream<JsonElement> stream() throws InvalidClassException{
        if(this.isArray())
            return StreamSupport.stream(this.getAsArray().spliterator(), false);

        throw new InvalidClassException("Unsupported data type: cannot create a Stream from this data type or convert it to a data type that could");
    }

    /**Creates a {@link Stream} from the content of the file (if it can -> JsonObject)
     *@return a {@link Stream} if the content is a JsonObject
     * @throws InvalidClassException if the content is not a JsonObject
     */
    public Stream<Map.Entry<String, JsonElement>> entryStream() throws InvalidClassException{
        if(this.isObject())
            return this.getAsObject().entrySet().stream();

        throw new InvalidClassException("Unsupported data type: cannot create a Stream from this data type or convert it to a data type that could");
    }
}
